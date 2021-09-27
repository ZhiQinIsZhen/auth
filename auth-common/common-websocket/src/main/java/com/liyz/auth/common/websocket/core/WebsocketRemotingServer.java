package com.liyz.auth.common.websocket.core;

import com.liyz.auth.common.websocket.config.WebsocketServerConfig;
import com.liyz.auth.common.websocket.constant.BizType;
import com.liyz.auth.common.websocket.processor.ServerHeartbeatProcessor;
import com.liyz.auth.common.websocket.processor.ServerOnRequestProcessor;
import com.liyz.auth.common.websocket.processor.ServerOnResponseProcessor;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/27 10:47
 */
@Slf4j
public class WebsocketRemotingServer extends AbstractWebsocketRemotingServer{

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public WebsocketRemotingServer(ThreadPoolExecutor messageExecutor, WebsocketServerConfig websocketServerConfig) {
        super(messageExecutor, websocketServerConfig);
    }

    @Override
    public void init() {
        // registry processor
        registerProcessor();
        if (initialized.compareAndSet(false, true)) {
            super.init();
        }
    }

    @Override
    public void destroyChannel(String channelId, Channel channel) {
        log.info("will destroy channel:{},channelId:{}", channel, channelId);
        channel.disconnect();
        channel.close();
    }

    private void registerProcessor() {
        // 1. registry on request message processor
        ServerOnRequestProcessor onRequestProcessor = new ServerOnRequestProcessor(this);
        super.registerProcessor(BizType.AUTH_BIZ_TYPE, onRequestProcessor, messageExecutor);
        // 2. registry on response message processor
        ServerOnResponseProcessor onResponseProcessor = new ServerOnResponseProcessor(getFutures());
        super.registerProcessor(BizType.AUTH_BIZ_TYPE, onResponseProcessor, messageExecutor);
        // 3. registry heartbeat message processor
        ServerHeartbeatProcessor heartbeatMessageProcessor = new ServerHeartbeatProcessor(this);
        super.registerProcessor(BizType.HEARTBEAT_BIZ_TYPE, heartbeatMessageProcessor, null);
    }
}
