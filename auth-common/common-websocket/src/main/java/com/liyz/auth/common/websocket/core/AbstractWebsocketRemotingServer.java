package com.liyz.auth.common.websocket.core;

import com.liyz.auth.common.websocket.config.WebsocketServerConfig;
import com.liyz.auth.common.websocket.handler.ServerHandler;
import com.liyz.auth.common.websocket.message.BaseMessage;
import com.liyz.auth.common.websocket.message.MessageService;
import com.liyz.auth.common.websocket.message.impl.HeartbeatMessage;
import com.liyz.auth.common.websocket.processor.MessageProcessor;
import io.netty.channel.Channel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 15:38
 */
public abstract class AbstractWebsocketRemotingServer extends AbstractWebsocketService implements MessageService {

    private final WebsocketBootstrap websocketBootstrap;

    @Override
    public void init() {
        super.init();
        websocketBootstrap.start();
    }

    public AbstractWebsocketRemotingServer(ThreadPoolExecutor messageExecutor, WebsocketServerConfig websocketServerConfig) {
        super(messageExecutor);
        this.websocketBootstrap = new WebsocketBootstrap(websocketServerConfig);
        this.websocketBootstrap.setChannelHandlers(new ServerHandler(lock, messageExecutor, processorTable));
    }

    @Override
    public Object sendSyncRequest(Channel channel, Object msg) throws TimeoutException {
        if (channel == null) {
            throw new RuntimeException("client is not connected");
        }
        //todo
        BaseMessage baseMessage = buildRequestMessage(msg, "1");
        return super.sendSync(channel, baseMessage, websocketBootstrap.getWebsocketServerConfig().getSocketRequestTimeout());
    }

    @Override
    public void sendAsyncRequest(Channel channel, Object msg) {
        if (channel == null) {
            throw new RuntimeException("client is not connected");
        }
        BaseMessage baseMessage = buildRequestMessage(msg, "2");
        super.sendAsync(channel, baseMessage);
    }

    @Override
    public void sendAsyncResponse(BaseMessage baseMessage, Channel channel, Object msg) {
        Channel clientChannel = channel;
        if (!(msg instanceof HeartbeatMessage)) {
            clientChannel = channel.isActive() ? channel : null;
        }
        if (clientChannel != null) {
            BaseMessage rpcMsg = buildResponseMessage(baseMessage, msg, msg instanceof HeartbeatMessage
                    ? "heartbeat"
                    : baseMessage.getBiz());
            super.sendAsync(clientChannel, rpcMsg);
        } else {
            throw new RuntimeException("channel is error.");
        }
    }

    @Override
    public void registerProcessor(String messageType, MessageProcessor messageProcessor, ExecutorService executor) {
        Pair<MessageProcessor, ExecutorService> pair = Pair.of(messageProcessor, executor);
        this.processorTable.put(messageType, pair);
    }

    /**
     * Sets listen port.
     *
     * @param listenPort the listen port
     */
    public void setListenPort(int listenPort) {
        websocketBootstrap.setListenPort(listenPort);
    }

    /**
     * Gets listen port.
     *
     * @return the listen port
     */
    public int getListenPort() {
        return websocketBootstrap.getListenPort();
    }

    @Override
    public void destroy() {
        websocketBootstrap.shutdown();
        super.destroy();
    }
}
