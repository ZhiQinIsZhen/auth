package com.liyz.auth.common.websocket.processor;

import com.liyz.auth.common.websocket.message.BaseMessage;
import com.liyz.auth.common.websocket.message.MessageService;
import io.netty.channel.ChannelHandlerContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/27 10:49
 */
public class ServerOnRequestProcessor implements MessageProcessor{

    private MessageService messageService;

    public ServerOnRequestProcessor(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws Exception {
        onRequestMessage(ctx, baseMessage);
    }

    private void onRequestMessage(ChannelHandlerContext ctx, BaseMessage baseMessage) {
        messageService.sendAsyncResponse(baseMessage, ctx.channel(), baseMessage.getResult());
    }
}
