package com.liyz.auth.common.websocket.config;

import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 11:13
 */
public class WebsocketServerConfig extends WebsocketBaseConfig{

    private static final String DEFAULT_BOSS_THREAD_PREFIX = "WebsocketBoss";
    private static final String EPOLL_WORKER_THREAD_PREFIX = "WebsocketServerEPollWorker";
    private static final String DEFAULT_NIO_WORKER_THREAD_PREFIX = "WebsocketServerNIOWorker";
    private static final int DEFAULT_BOSS_THREAD_SIZE = 1;
    private static final int DEFAULT_SHUTDOWN_TIMEOUT_SEC = 3;
    private static final int SOCKET_REQUEST_TIMEOUT = 30 * 1000;
    private static final String DEFAULT_SOCKET_PATH = "liyz";

    @Getter
    @Setter
    private int soBackLogSize = 1024;
    @Getter
    @Setter
    private int serverSocketSendBufSize = 153600;
    @Getter
    @Setter
    private int serverSocketResvBufSize = 153600;
    @Getter
    @Setter
    private int writeBufferLowWaterMark = 1048576;
    @Getter
    @Setter
    private int writeBufferHighWaterMark = 67108864;

    public WebsocketServerConfig(WebsocketProperties websocketProperties) {
        super(websocketProperties);
    }

    /**
     * is epoll mode
     *
     * @return
     */
    public boolean enableEpoll() {
        return serverChannelClazz.equals(EpollServerSocketChannel.class)
                && Epoll.isAvailable();
    }

    /**
     * Get boss thread size int.
     *
     * @return the int
     */
    public int getBossThreadSize() {
        return websocketProperties.getThreadFactory().getBossThreadSize() == 0 ?
                DEFAULT_BOSS_THREAD_SIZE : websocketProperties.getThreadFactory().getBossThreadSize();
    }

    /**
     * Gets server worker threads.
     *
     * @return the server worker threads
     */
    public int getServerWorkerThreads() {
        return workerThreadSize;
    }

    /**
     * Get boss thread prefix string.
     *
     * @return the string
     */
    public String getBossThreadPrefix() {
        return Objects.isNull(websocketProperties.getThreadFactory().getBossThreadPrefix()) ?
                DEFAULT_BOSS_THREAD_PREFIX : websocketProperties.getThreadFactory().getBossThreadPrefix();
    }

    /**
     * Get worker thread prefix string.
     *
     * @return the string
     */
    public String getWorkerThreadPrefix() {
        return Objects.isNull(websocketProperties.getThreadFactory().getWorkerThreadPrefix()) ? enableEpoll()
                ? EPOLL_WORKER_THREAD_PREFIX : DEFAULT_NIO_WORKER_THREAD_PREFIX
                : websocketProperties.getThreadFactory().getWorkerThreadPrefix();
    }

    /**
     * Get the timeout seconds of shutdown.
     *
     * @return the int
     */
    public int getServerShutdownWaitTime() {
        return websocketProperties.getShutdownWait() == 0
                ? DEFAULT_SHUTDOWN_TIMEOUT_SEC : websocketProperties.getShutdownWait();
    }

    /**
     * Gets socket request timeout.
     *
     * @return the rpc request timeout
     */
    public int getSocketRequestTimeout() {
        return SOCKET_REQUEST_TIMEOUT;
    }

    public String getSocketPath() {
        if (StringUtils.isBlank(websocketProperties.getSocketPath())) {
            return DEFAULT_SOCKET_PATH;
        }
        return websocketProperties.getSocketPath();
    }
}
