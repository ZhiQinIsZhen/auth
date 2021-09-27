package com.liyz.auth.common.websocket.handler;

import com.liyz.auth.common.websocket.message.BaseMessage;
import com.liyz.auth.common.websocket.processor.MessageProcessor;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 15:43
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends ChannelDuplexHandler {

    private final Object lock;
    protected final ThreadPoolExecutor messageExecutor;
    private final HashMap<String/*MessageType*/, Pair<MessageProcessor, ExecutorService>> processorTable;

    public ServerHandler(Object lock, ThreadPoolExecutor messageExecutor,
                         HashMap<String/*MessageType*/, Pair<MessageProcessor, ExecutorService>> processorTable) {
        this.lock = lock;
        this.messageExecutor = messageExecutor;
        this.processorTable = processorTable;
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BaseMessage) {
            processMessage(ctx, (BaseMessage) msg);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        synchronized (lock) {
            if (ctx.channel().isWritable()) {
                lock.notifyAll();
            }
        }
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (messageExecutor.isShutdown()) {
            return;
        }
        log.info("channel inactive: {}", ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("channel exx:" + cause.getMessage() + ",channel:" + ctx.channel());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("idle:" + evt);
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("channel:" + ctx.channel() + " read idle.");
                try {
                    closeChannelHandlerContext(ctx);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        log.info(ctx + " will closed");
        super.close(ctx, promise);
    }

    private void closeChannelHandlerContext(ChannelHandlerContext ctx) {
        log.info("closeChannelHandlerContext channel:" + ctx.channel());
        ctx.disconnect();
        ctx.close();
    }

    /**
     * For testing. When the thread pool is full, you can change this variable and share the stack
     */
    boolean allowDumpStack = false;

    /**
     * message processing.
     *
     * @param ctx
     * @param baseMessage
     * @throws Exception
     */
    protected void processMessage(ChannelHandlerContext ctx, BaseMessage baseMessage) throws Exception {
        log.info(String.format("%s msgId:%s, body:%s", this, baseMessage.getId(), baseMessage.toString()));
        String biz = baseMessage.getBiz();
        final Pair<MessageProcessor, ExecutorService> pair = this.processorTable.get(biz);
        if (Objects.nonNull(pair)) {
            if (Objects.nonNull(pair.getValue())) {
                try {
                    pair.getValue().execute(() -> {
                        try {
                            pair.getKey().process(ctx, baseMessage);
                        } catch (Throwable th) {
                            log.error(th.getMessage(), th);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    log.error("thread pool is full, current max pool size is " + messageExecutor.getActiveCount());
                    dumpStack();
                }
            } else {
                try {
                    pair.getKey().process(ctx, baseMessage);
                } catch (Throwable th) {
                    log.error(th.getMessage(), th);
                }
            }
        } else {
            log.error("This message biz [{}] has no processor.", biz);
        }
    }

    /**
     * dumpStack
     */
    private void dumpStack() {
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
}
