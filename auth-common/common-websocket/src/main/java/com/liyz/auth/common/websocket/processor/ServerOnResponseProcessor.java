package com.liyz.auth.common.websocket.processor;

import com.liyz.auth.common.websocket.message.BaseMessage;
import com.liyz.auth.common.websocket.message.MessageFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/27 11:13
 */
public class ServerOnResponseProcessor implements MessageProcessor{

    private ConcurrentMap<String, MessageFuture> futures;

    public ServerOnResponseProcessor(ConcurrentMap<String, MessageFuture> futures) {
        this.futures = futures;
    }

    @Override
    public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws Exception {
        MessageFuture messageFuture = futures.remove(baseMessage.getId());
        if (messageFuture != null) {
            messageFuture.setResultMessage(baseMessage.getResult());
        } else {
            onResponseMessage(ctx, baseMessage);
        }
    }

    private void onResponseMessage(ChannelHandlerContext ctx, BaseMessage baseMessage) {
        ctx.writeAndFlush(baseMessage.getResult());
    }
}
