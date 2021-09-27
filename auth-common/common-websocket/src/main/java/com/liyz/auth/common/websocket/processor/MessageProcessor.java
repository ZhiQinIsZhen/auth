package com.liyz.auth.common.websocket.processor;

import com.liyz.auth.common.websocket.message.BaseMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 15:52
 */
public interface MessageProcessor {

    /**
     * Process message
     *
     * @param ctx
     * @param baseMessage
     * @throws Exception
     */
    void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws Exception;
}
