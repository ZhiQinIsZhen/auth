package com.liyz.auth.common.tcp.core.rpc.processor;

import com.liyz.auth.common.tcp.core.protocol.RegisterTMRequest;
import com.liyz.auth.common.tcp.core.protocol.RegisterTMResponse;
import com.liyz.auth.common.tcp.core.protocol.RpcMessage;
import com.liyz.auth.common.tcp.core.protocol.Version;
import com.liyz.auth.common.tcp.core.rpc.ChannelManager;
import com.liyz.auth.common.tcp.core.rpc.RegisterCheckAuthHandler;
import com.liyz.auth.common.tcp.core.rpc.RemotingProcessor;
import com.liyz.auth.common.tcp.core.rpc.RemotingServer;
import com.liyz.auth.common.tcp.loader.EnhancedServiceLoader;
import com.liyz.auth.common.tcp.util.NetUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:50
 */
@Slf4j
public class RegTmProcessor implements RemotingProcessor {

    private RemotingServer remotingServer;

    private RegisterCheckAuthHandler checkAuthHandler;

    public RegTmProcessor(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
        this.checkAuthHandler = EnhancedServiceLoader.load(RegisterCheckAuthHandler.class);
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        onRegTmMessage(ctx, rpcMessage);
    }

    private void onRegTmMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        RegisterTMRequest message = (RegisterTMRequest) rpcMessage.getBody();
        String ipAndPort = NetUtil.toStringAddress(ctx.channel().remoteAddress());
        Version.putChannelVersion(ctx.channel(), message.getVersion());
        boolean isSuccess = false;
        String errorInfo = StringUtils.EMPTY;
        try {
            if (null == checkAuthHandler || checkAuthHandler.regTransactionManagerCheckAuth(message)) {
                ChannelManager.registerTMChannel(message, ctx.channel());
                Version.putChannelVersion(ctx.channel(), message.getVersion());
                isSuccess = true;
                if (log.isDebugEnabled()) {
                    log.debug("checkAuth for client:{},vgroup:{},applicationId:{}",
                            ipAndPort, message.getTransactionServiceGroup(), message.getApplicationId());
                }
            }
        } catch (Exception exx) {
            isSuccess = false;
            errorInfo = exx.getMessage();
            log.error("TM register fail, error message:{}", errorInfo);
        }
        RegisterTMResponse response = new RegisterTMResponse(isSuccess);
        if (StringUtils.isNotEmpty(errorInfo)) {
            response.setMsg(errorInfo);
        }
        remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), response);
        if (log.isInfoEnabled()) {
            log.info("TM register success,message:{},channel:{},client version:{}", message, ctx.channel(),
                    message.getVersion());
        }
    }
}
