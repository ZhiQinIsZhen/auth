package com.liyz.auth.common.netty.processor;

import com.liyz.auth.common.netty.core.ChannelManager;
import com.liyz.auth.common.netty.core.RpcContext;
import com.liyz.auth.common.netty.protocol.MessageFuture;
import com.liyz.auth.common.netty.protocol.RpcMessage;
import com.liyz.auth.common.netty.util.NetUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 11:11
 */
@Slf4j
public class ServerOnResponseProcessor implements RemotingProcessor {

    /**
     * The Futures from io.seata.core.rpc.netty.AbstractNettyRemoting#futures
     */
    private ConcurrentMap<Integer, MessageFuture> futures;

    public ServerOnResponseProcessor(ConcurrentHashMap<Integer, MessageFuture> futures) {
        this.futures = futures;
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        MessageFuture messageFuture = futures.remove(rpcMessage.getId());
        if (messageFuture != null) {
            messageFuture.setResultMessage(rpcMessage.getBody());
        } else {
            if (ChannelManager.isRegistered(ctx.channel())) {
                onResponseMessage(ctx, rpcMessage);
            } else {
                try {
                    if (log.isInfoEnabled()) {
                        log.info("closeChannelHandlerContext channel:" + ctx.channel());
                    }
                    ctx.disconnect();
                    ctx.close();
                } catch (Exception exx) {
                    log.error(exx.getMessage());
                }
                if (log.isInfoEnabled()) {
                    log.info(String.format("close a unhandled connection! [%s]", ctx.channel().toString()));
                }
            }
        }
    }

    private void onResponseMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        if (log.isDebugEnabled()) {
            log.debug("server received:{},clientIp:{},vgroup:{}", rpcMessage.getBody(),
                    NetUtil.toIpAddress(ctx.channel().remoteAddress()),
                    ChannelManager.getContextFromIdentified(ctx.channel()).getTransactionServiceGroup());
        }
//        if (rpcMessage.getBody() instanceof AbstractResultMessage) {
//            RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
//        }
    }
}
