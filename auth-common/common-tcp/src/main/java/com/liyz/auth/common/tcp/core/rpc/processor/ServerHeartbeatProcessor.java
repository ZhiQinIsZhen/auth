package com.liyz.auth.common.tcp.core.rpc.processor;

import com.liyz.auth.common.tcp.core.protocol.HeartbeatMessage;
import com.liyz.auth.common.tcp.core.protocol.RpcMessage;
import com.liyz.auth.common.tcp.core.rpc.RemotingProcessor;
import com.liyz.auth.common.tcp.core.rpc.RemotingServer;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:52
 */
@Slf4j
public class ServerHeartbeatProcessor implements RemotingProcessor {

    private RemotingServer remotingServer;

    public ServerHeartbeatProcessor(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        try {
            remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), HeartbeatMessage.PONG);
        } catch (Throwable throwable) {
            log.error("send response error: {}", throwable.getMessage(), throwable);
        }
        if (log.isDebugEnabled()) {
            log.debug("received PING from {}", ctx.channel().remoteAddress());
        }
    }
}
