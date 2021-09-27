package com.liyz.auth.common.websocket.core;

import com.google.common.collect.Lists;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.common.websocket.hook.MessageHook;
import com.liyz.auth.common.websocket.hook.StatusMessageHook;
import com.liyz.auth.common.websocket.message.BaseMessage;
import com.liyz.auth.common.websocket.message.MessageFuture;
import com.liyz.auth.common.websocket.processor.MessageProcessor;
import com.liyz.auth.common.websocket.thread.NamedThreadFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 14:10
 */
@Slf4j
public abstract class AbstractWebsocketService implements Disposable{

    /**
     * The Now mills.
     */
    protected volatile long nowMills = 0;
    private static final int TIMEOUT_CHECK_INTERNAL = 10000;
    private static final long NOT_WRITEABLE_CHECK_MILLS = 10L;
    protected final Object lock = new Object();

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
     * Obtain the return result through MessageFuture blocking.
     *
     * @see AbstractWebsocketService#sendSync
     */
    @Getter
    protected final ConcurrentHashMap<String, MessageFuture> futures = new ConcurrentHashMap<>();

    protected final List<MessageHook> messageHooks = Lists.newArrayList(new StatusMessageHook());

    /**
     * This container holds all processors.
     * processor type
     */
    protected final HashMap<String/*MessageType*/, Pair<MessageProcessor, ExecutorService>> processorTable = new HashMap<>(32);

    /**
     * init timer executor
     */
    public void init() {
        timerExecutor.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, MessageFuture> entry : futures.entrySet()) {
                if (entry.getValue().isTimeout()) {
                    futures.remove(entry.getKey());
                    entry.getValue().setResultMessage(null);
                    log.info("timeout clear future: {}", entry.getValue().getBaseMessage().toString());
                }
            }

            nowMills = System.currentTimeMillis();
        }, TIMEOUT_CHECK_INTERNAL, TIMEOUT_CHECK_INTERNAL, TimeUnit.MILLISECONDS);
    }

    public AbstractWebsocketService(ThreadPoolExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    /**
     * Destroy channel.
     *
     * @param channel
     */
    public void destroyChannel(Channel channel) {
        destroyChannel(channel.id().asLongText(), channel);
    }

    /**
     * Destroy channel.
     *
     * @param channelId the server id
     * @param channel       the channel
     */
    public abstract void destroyChannel(String channelId, Channel channel);

    /**
     * rpc sync request
     * Obtain the return result through MessageFuture blocking.
     *
     * @param channel
     * @param baseMessage
     * @param timeoutMillis
     * @return
     */
    protected Object sendSync(Channel channel, BaseMessage baseMessage, long timeoutMillis) throws TimeoutException {
        if (timeoutMillis <= 0) {
            throw new RemoteServiceException("timeout should more than 0ms");
        }
        if (channel == null) {
            log.warn("sendSync nothing, caused by null channel.");
            return null;
        }

        MessageFuture messageFuture = new MessageFuture();
        messageFuture.setBaseMessage(baseMessage);
        messageFuture.setTimeout(timeoutMillis);
        futures.put(baseMessage.getId(), messageFuture);

        channelWritableCheck(channel, baseMessage.msgMap());
        String channelId = channel.id().asLongText();

        doBeforeRpcHooks(channelId, baseMessage);

        channel.writeAndFlush(baseMessage).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                MessageFuture messageFuture1 = futures.remove(baseMessage.getId());
                if (messageFuture1 != null) {
                    messageFuture1.setResultMessage(future.cause());
                }
                destroyChannel(future.channel());
            }
        });

        try {
            Object result = messageFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
            doAfterRpcHooks(channelId, baseMessage, result);
            return result;
        } catch (Exception exx) {
            log.error("wait response error:{},ip:{},request:{}", exx.getMessage(), channel.remoteAddress(),
                    baseMessage.toString());
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
     * @param baseMessage
     */
    protected void sendAsync(Channel channel, BaseMessage baseMessage) {
        channelWritableCheck(channel, baseMessage);

        log.info("write message:" + baseMessage.getMsg() + ", channel:" + channel + ",active?"
                + channel.isActive() + ",writable?" + channel.isWritable() + ",isopen?" + channel.isOpen());

        doBeforeRpcHooks(channel.id().asLongText(), baseMessage);

        channel.writeAndFlush(baseMessage).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                destroyChannel(future.channel());
            }
        });
    }

    protected BaseMessage buildRequestMessage(Object msg, String biz) {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setId(UUID.randomUUID().toString());
        baseMessage.setBiz(biz);
        baseMessage.setBiz(msg.toString());
        return baseMessage;
    }

    protected BaseMessage buildResponseMessage(BaseMessage requestMsg, Object msg, String biz) {
        BaseMessage responseMsg = new BaseMessage();
        responseMsg.setId(requestMsg.getId());
        responseMsg.setBiz(biz);
        responseMsg.setBiz(msg.toString());
        return responseMsg;
    }

    /**
     * Gets address from context.
     *
     * @param ctx the ctx
     * @return the address from context
     */
    protected String getChannelIdFromContext(ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText();
    }

    private void channelWritableCheck(Channel channel, Object msg) {
        int tryTimes = 0;
        synchronized (lock) {
            while (!channel.isWritable()) {
                try {
                    tryTimes++;
                    if (tryTimes > 2000) {
                        destroyChannel(channel);
                        throw new RemoteServiceException("retry count > 2000; msg:" + ((msg == null) ? "null" : msg.toString()));
                    }
                    lock.wait(NOT_WRITEABLE_CHECK_MILLS);
                } catch (InterruptedException exx) {
                    log.error(exx.getMessage());
                }
            }
        }
    }

    protected void doBeforeRpcHooks(String channelId, BaseMessage baseMessage) {
        for (MessageHook messageHook: messageHooks) {
            messageHook.doBeforeRequest(channelId, baseMessage);
        }
    }

    protected void doAfterRpcHooks(String channelId, BaseMessage baseMessage, Object response) {
        for (MessageHook messageHook: messageHooks) {
            messageHook.doAfterResponse(channelId, baseMessage, response);
        }
    }

    @Override
    public void destroy() {
        timerExecutor.shutdown();
        messageExecutor.shutdown();
    }
}
