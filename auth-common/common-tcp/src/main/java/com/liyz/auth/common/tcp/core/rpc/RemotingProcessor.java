package com.liyz.auth.common.tcp.core.rpc;

import com.liyz.auth.common.tcp.core.protocol.RpcMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 注释:The remoting processor
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 15:59
 */
public interface RemotingProcessor {

    /**
     * Process message
     *
     * @param ctx
     * @param rpcMessage
     * @throws Exception
     */
    void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception;
}
