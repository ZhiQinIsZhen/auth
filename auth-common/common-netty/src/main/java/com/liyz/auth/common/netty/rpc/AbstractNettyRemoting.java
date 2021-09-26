package com.liyz.auth.common.netty.rpc;

import com.google.common.collect.Lists;
import com.liyz.auth.common.netty.core.Disposable;
import com.liyz.auth.common.netty.hook.RpcHook;
import com.liyz.auth.common.netty.hook.StatusRpcHook;
import com.liyz.auth.common.netty.processor.RemotingProcessor;
import com.liyz.auth.common.netty.protocol.*;
import com.liyz.auth.common.netty.thread.NamedThreadFactory;
import com.liyz.auth.common.netty.thread.PositiveAtomicCounter;
import com.liyz.auth.common.netty.util.ChannelUtil;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/22 14:26
 */
@Slf4j
public abstract class AbstractNettyRemoting implements Disposable {

    private static final long NOT_WRITEABLE_CHECK_MILLS = 10L;

    /**
     * The Now mills.
     */
    protected volatile long nowMills = 0;
    private static final int TIMEOUT_CHECK_INTERNAL = 10000;
    protected final Object lock = new Object();

    /**
     * The Is sending.
     */
    protected volatile boolean isSending = false;

    /**
     * The Timer executor.
     */
    protected final ScheduledExecutorService timerExecutor = new ScheduledThreadPoolExecutor(1,
            new NamedThreadFactory("timeoutChecker", 1, true));

    /**
     * The Message executor.
     */
    protected final ThreadPoolExecutor messageExecutor;

    /**
     * Id generator of this remoting
     */
    protected final PositiveAtomicCounter idGenerator = new PositiveAtomicCounter();

    /**
     * Obtain the return result through MessageFuture blocking.
     *
     * @see AbstractNettyRemoting#sendSync
     */
    protected final ConcurrentHashMap<Integer, MessageFuture> futures = new ConcurrentHashMap<>();

    /**
     * This container holds all processors.
     * processor type {@link MessageType}
     */
    protected final HashMap<Integer, Pair<RemotingProcessor, ExecutorService>> processorTable = new HashMap<>(32);

    protected final List<RpcHook> rpcHooks = Lists.newArrayList(new StatusRpcHook());

    public void init() {
        timerExecutor.scheduleAtFixedRate(() -> {
            for (Map.Entry<Integer, MessageFuture> entry : futures.entrySet()) {
                if (entry.getValue().isTimeout()) {
                    futures.remove(entry.getKey());
                    entry.getValue().setResultMessage(null);
                    log.info("timeout clear future: {}", entry.getValue().getRequestMessage().getBody());
                }
            }

            nowMills = System.currentTimeMillis();
        }, TIMEOUT_CHECK_INTERNAL, TIMEOUT_CHECK_INTERNAL, TimeUnit.MILLISECONDS);
    }

    public AbstractNettyRemoting(ThreadPoolExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    public int getNextMessageId() {
        return idGenerator.incrementAndGet();
    }

    public ConcurrentHashMap<Integer, MessageFuture> getFutures() {
        return futures;
    }

    public void destroyChannel(Channel channel) {
        destroyChannel(getAddressFromChannel(channel), channel);
    }

    @Override
    public void destroy() {
        timerExecutor.shutdown();
        messageExecutor.shutdown();
    }

    /**
     * rpc sync request
     * Obtain the return result through MessageFuture blocking.
     *
     * @param channel
     * @param rpcMessage
     * @param timeoutMillis
     * @return
     */
    protected Object sendSync(Channel channel, RpcMessage rpcMessage, long timeoutMillis) throws TimeoutException {
        if (timeoutMillis <= 0) {
            throw new RemoteServiceException("timeout should more than 0ms");
        }
        if (channel == null) {
            log.warn("sendSync nothing, caused by null channel.");
            return null;
        }

        MessageFuture messageFuture = new MessageFuture();
        messageFuture.setRequestMessage(rpcMessage);
        messageFuture.setTimeout(timeoutMillis);
        futures.put(rpcMessage.getId(), messageFuture);

        channelWritableCheck(channel, rpcMessage.getBody());

        String remoteAddr = ChannelUtil.getAddressFromChannel(channel);
        doBeforeRpcHooks(remoteAddr, rpcMessage);

        channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                MessageFuture messageFuture1 = futures.remove(rpcMessage.getId());
                if (messageFuture1 != null) {
                    messageFuture1.setResultMessage(future.cause());
                }
                destroyChannel(future.channel());
            }
        });

        try {
            Object result = messageFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            doAfterRpcHooks(remoteAddr, rpcMessage, result);
            return result;
        } catch (Exception exx) {
            log.error("wait response error:{},ip:{},request:{}", exx.getMessage(), channel.remoteAddress(),
                    rpcMessage.getBody());
            if (exx instanceof TimeoutException) {
                throw (TimeoutException) exx;
            } else {
                throw new RuntimeException(exx);
            }
        }
    }

    /**
     * rpc async request.
     *
     * @param channel
     * @param rpcMessage
     */
    protected void sendAsync(Channel channel, RpcMessage rpcMessage) {
        channelWritableCheck(channel, rpcMessage.getBody());

        log.info("write message:" + rpcMessage.getBody() + ", channel:" + channel + ",active?"
                + channel.isActive() + ",writable?" + channel.isWritable() + ",isopen?" + channel.isOpen());

        doBeforeRpcHooks(ChannelUtil.getAddressFromChannel(channel), rpcMessage);

        channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                destroyChannel(future.channel());
            }
        });
    }

    protected RpcMessage buildRequestMessage(Object msg, byte messageType) {
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setId(getNextMessageId());
        rpcMessage.setMessageType(messageType);
        rpcMessage.setCodec(ProtocolConstants.CONFIGURED_CODEC);
        rpcMessage.setCompressor(ProtocolConstants.CONFIGURED_COMPRESSOR);
        rpcMessage.setBody(msg);
        return rpcMessage;
    }

    protected RpcMessage buildResponseMessage(RpcMessage rpcMessage, Object msg, byte messageType) {
        RpcMessage rpcMsg = new RpcMessage();
        rpcMsg.setMessageType(messageType);
        rpcMsg.setCodec(rpcMessage.getCodec()); // same with request
        rpcMsg.setCompressor(rpcMessage.getCompressor());
        rpcMsg.setBody(msg);
        rpcMsg.setId(rpcMessage.getId());
        return rpcMsg;
    }

    /**
     * For testing. When the thread pool is full, you can change this variable and share the stack
     */
    boolean allowDumpStack = false;

    /**
     * Rpc message processing.
     *
     * @param ctx
     * @param rpcMessage
     */
    protected void processMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        log.info(String.format("%s msgId:%s, body:%s", this, rpcMessage.getId(), rpcMessage.getBody()));
        Object body = rpcMessage.getBody();
        if (body instanceof MessageTypeAware) {
            MessageTypeAware messageTypeAware = (MessageTypeAware) body;
            final Pair<RemotingProcessor, ExecutorService> pair = this.processorTable.get((int) messageTypeAware.getTypeCode());
            if (pair != null) {
                if (pair.getRight() != null) {
                    try {
                        pair.getRight().execute(() -> {
                            try {
                                pair.getLeft().process(ctx, rpcMessage);
                            } catch (Throwable th) {
                                log.error(th.getMessage(), th);
                            }
                        });
                    } catch (RejectedExecutionException e) {
                        log.error("thread pool is full, current max pool size is " + messageExecutor.getActiveCount());
                        if (allowDumpStack) {
                            String name = ManagementFactory.getRuntimeMXBean().getName();
                            String pid = name.split("@")[0];
                            int idx = new Random().nextInt(100);
                            try {
                                Runtime.getRuntime().exec("jstack " + pid + " >d:/" + idx + ".log");
                            } catch (IOException exx) {
                                log.error(exx.getMessage());
                            }
                            allowDumpStack = false;
                        }
                    }
                } else {
                    try {
                        pair.getLeft().process(ctx, rpcMessage);
                    } catch (Throwable th) {
                        log.error(th.getMessage(), th);
                    }
                }
            } else {
                log.error("This message type [{}] has no processor.", messageTypeAware.getTypeCode());
            }
        } else {
            log.error("This rpcMessage body[{}] is not MessageTypeAware type.", body);
        }
    }

    /**
     * Gets address from context.
     *
     * @param ctx the ctx
     * @return the address from context
     */
    protected String getAddressFromContext(ChannelHandlerContext ctx) {
        return getAddressFromChannel(ctx.channel());
    }

    /**
     * Gets address from channel.
     *
     * @param channel the channel
     * @return the address from channel
     */
    protected String getAddressFromChannel(Channel channel) {
        SocketAddress socketAddress = channel.remoteAddress();
        String address = socketAddress.toString();
        if (socketAddress.toString().indexOf("/") == 0) {
            address = socketAddress.toString().substring("/".length());
        }
        return address;
    }

    private void channelWritableCheck(Channel channel, Object msg) {
        int tryTimes = 0;
        synchronized (lock) {
            while (!channel.isWritable()) {
                try {
                    tryTimes++;
                    if (tryTimes > 2000) {
                        destroyChannel(channel);
                        throw new RemoteServiceException("msg:" + ((msg == null) ? "null" : msg.toString()));
                    }
                    lock.wait(NOT_WRITEABLE_CHECK_MILLS);
                } catch (InterruptedException exx) {
                    log.error(exx.getMessage());
                }
            }
        }
    }

    /**
     * Destroy channel.
     *
     * @param serverAddress the server address
     * @param channel       the channel
     */
    public abstract void destroyChannel(String serverAddress, Channel channel);

    protected void doBeforeRpcHooks(String remoteAddr, RpcMessage request) {
        for (RpcHook rpcHook: rpcHooks) {
            rpcHook.doBeforeRequest(remoteAddr, request);
        }
    }

    protected void doAfterRpcHooks(String remoteAddr, RpcMessage request, Object response) {
        for (RpcHook rpcHook: rpcHooks) {
            rpcHook.doAfterResponse(remoteAddr, request, response);
        }
    }
}
