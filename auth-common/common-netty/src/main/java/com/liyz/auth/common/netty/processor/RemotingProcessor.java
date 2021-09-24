package com.liyz.auth.common.netty.processor;

import com.liyz.auth.common.netty.protocol.RpcMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 10:15
 */
public interface RemotingProcessor {

    /**
     * Process message
     *
     * @param ctx        Channel handler context.
     * @param rpcMessage rpc message.
     * @throws Exception throws exception process message error.
     */
    void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception;
}
