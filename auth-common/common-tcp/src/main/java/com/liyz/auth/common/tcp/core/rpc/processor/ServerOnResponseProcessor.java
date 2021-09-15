package com.liyz.auth.common.tcp.core.rpc.processor;

import com.liyz.auth.common.tcp.core.protocol.AbstractResultMessage;
import com.liyz.auth.common.tcp.core.protocol.MessageFuture;
import com.liyz.auth.common.tcp.core.protocol.RpcMessage;
import com.liyz.auth.common.tcp.core.rpc.ChannelManager;
import com.liyz.auth.common.tcp.core.rpc.RemotingProcessor;
import com.liyz.auth.common.tcp.core.rpc.RpcContext;
import com.liyz.auth.common.tcp.core.rpc.TransactionMessageHandler;
import com.liyz.auth.common.tcp.util.NetUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:46
 */
@Slf4j
public class ServerOnResponseProcessor implements RemotingProcessor {

    /**
     * To handle the received RPC message on upper level.
     */
    private TransactionMessageHandler transactionMessageHandler;

    /**
     * The Futures from io.seata.core.rpc.netty.AbstractNettyRemoting#futures
     */
    private ConcurrentMap<Integer, MessageFuture> futures;

    public ServerOnResponseProcessor(TransactionMessageHandler transactionMessageHandler,
                                     ConcurrentHashMap<Integer, MessageFuture> futures) {
        this.transactionMessageHandler = transactionMessageHandler;
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
        } else {
            try {
                BatchLogHandler.INSTANCE.getLogQueue()
                        .put(rpcMessage.getBody() + ",clientIp:" + NetUtil.toIpAddress(ctx.channel().remoteAddress()) + ",vgroup:"
                                + ChannelManager.getContextFromIdentified(ctx.channel()).getTransactionServiceGroup());
            } catch (InterruptedException e) {
                log.error("put message to logQueue error: {}", e.getMessage(), e);
            }
        }
        if (rpcMessage.getBody() instanceof AbstractResultMessage) {
            RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
            transactionMessageHandler.onResponse((AbstractResultMessage) rpcMessage.getBody(), rpcContext);
        }
    }
}
