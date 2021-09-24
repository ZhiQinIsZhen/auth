package com.liyz.auth.common.netty.processor;

import com.liyz.auth.common.netty.core.ChannelManager;
import com.liyz.auth.common.netty.core.RpcContext;
import com.liyz.auth.common.netty.protocol.AbstractMessage;
import com.liyz.auth.common.netty.protocol.RpcMessage;
import com.liyz.auth.common.netty.rpc.RemotingServer;
import com.liyz.auth.common.netty.util.NetUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 10:16
 */
@Slf4j
public class ServerOnRequestProcessor implements RemotingProcessor{

    private RemotingServer remotingServer;

    public ServerOnRequestProcessor(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        if (ChannelManager.isRegistered(ctx.channel())) {
            onRequestMessage(ctx, rpcMessage);
        } else {
            try {
                log.info("closeChannelHandlerContext channel:" + ctx.channel());
                ctx.disconnect();
                ctx.close();
            } catch (Exception exx) {
                log.error(exx.getMessage());
            }
            log.info(String.format("close a unhandled connection! [%s]", ctx.channel().toString()));
        }
    }

    private void onRequestMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        Object message = rpcMessage.getBody();
        RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
        if (log.isDebugEnabled()) {
            log.debug("server received:{},clientIp:{},vgroup:{}", message,
                    NetUtil.toIpAddress(ctx.channel().remoteAddress()), rpcContext.getTransactionServiceGroup());
        }
        if (!(message instanceof AbstractMessage)) {
            return;
        }
        remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), message);
    }
}
