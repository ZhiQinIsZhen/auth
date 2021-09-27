package com.liyz.auth.common.websocket.processor;

import com.liyz.auth.common.websocket.message.BaseMessage;
import com.liyz.auth.common.websocket.message.MessageService;
import com.liyz.auth.common.websocket.message.impl.HeartbeatMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/27 11:24
 */
@Slf4j
public class ServerHeartbeatProcessor implements MessageProcessor{

    private MessageService messageService;

    public ServerHeartbeatProcessor(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws Exception {
        try {
            messageService.sendAsyncResponse(baseMessage, ctx.channel(), HeartbeatMessage.PONG);
        } catch (Throwable throwable) {
            log.error("send response error: {}", throwable.getMessage(), throwable);
        }
        log.info("received PING from {}", ctx.channel().remoteAddress());
    }
}
