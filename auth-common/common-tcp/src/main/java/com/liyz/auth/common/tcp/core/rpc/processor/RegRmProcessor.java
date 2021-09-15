package com.liyz.auth.common.tcp.core.rpc.processor;

import com.liyz.auth.common.tcp.core.protocol.RegisterRMRequest;
import com.liyz.auth.common.tcp.core.protocol.RegisterRMResponse;
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
 * @date 2021/9/14 9:47
 */
@Slf4j
public class RegRmProcessor implements RemotingProcessor {

    private RemotingServer remotingServer;

    private RegisterCheckAuthHandler checkAuthHandler;

    public RegRmProcessor(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
        this.checkAuthHandler = EnhancedServiceLoader.load(RegisterCheckAuthHandler.class);
    }

    @Override
    public void process(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        onRegRmMessage(ctx, rpcMessage);
    }

    private void onRegRmMessage(ChannelHandlerContext ctx, RpcMessage rpcMessage) {
        RegisterRMRequest message = (RegisterRMRequest) rpcMessage.getBody();
        String ipAndPort = NetUtil.toStringAddress(ctx.channel().remoteAddress());
        boolean isSuccess = false;
        String errorInfo = StringUtils.EMPTY;
        try {
            if (null == checkAuthHandler || checkAuthHandler.regResourceManagerCheckAuth(message)) {
                ChannelManager.registerRMChannel(message, ctx.channel());
                Version.putChannelVersion(ctx.channel(), message.getVersion());
                isSuccess = true;
                if (log.isDebugEnabled()) {
                    log.debug("checkAuth for client:{},vgroup:{},applicationId:{} is OK", ipAndPort, message.getTransactionServiceGroup(), message.getApplicationId());
                }
            }
        } catch (Exception exx) {
            isSuccess = false;
            errorInfo = exx.getMessage();
            log.error("RM register fail, error message:{}", errorInfo);
        }
        RegisterRMResponse response = new RegisterRMResponse(isSuccess);
        if (StringUtils.isNotEmpty(errorInfo)) {
            response.setMsg(errorInfo);
        }
        remotingServer.sendAsyncResponse(rpcMessage, ctx.channel(), response);
        if (log.isInfoEnabled()) {
            log.info("RM register success,message:{},channel:{},client version:{}", message, ctx.channel(),
                    message.getVersion());
        }
    }
}
