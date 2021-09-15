package com.liyz.auth.common.tcp.core.rpc;

import com.liyz.auth.common.tcp.core.NettyBaseConfig;
import com.liyz.auth.common.tcp.v1.properties.TransportProperties;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import org.apache.commons.lang3.StringUtils;

import static com.liyz.auth.common.tcp.core.DefaultValues.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:30
 */
public class NettyServerConfig extends NettyBaseConfig {

    private int serverSelectorThreads = WORKER_THREAD_SIZE;
    private int serverSocketSendBufSize = 153600;
    private int serverSocketResvBufSize = 153600;
    private int serverWorkerThreads = WORKER_THREAD_SIZE;
    private int soBackLogSize = 1024;
    private int writeBufferHighWaterMark = 67108864;
    private int writeBufferLowWaterMark = 1048576;
    private static final int DEFAULT_LISTEN_PORT = 8091;
    private static final int RPC_REQUEST_TIMEOUT = 30 * 1000;
    private int serverChannelMaxIdleTimeSeconds = 30;
    private static final String EPOLL_WORKER_THREAD_PREFIX = "NettyServerEPollWorker";

    public NettyServerConfig(TransportProperties transportProperties) {
        super(transportProperties);
    }


    /**
     * Gets server selector threads.
     *
     * @return the server selector threads
     */
    public int getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    /**
     * Sets server selector threads.
     *
     * @param serverSelectorThreads the server selector threads
     */
    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    /**
     * Enable epoll boolean.
     *
     * @return the boolean
     */
    public boolean enableEpoll() {
        return SERVER_CHANNEL_CLAZZ.equals(EpollServerSocketChannel.class)
                && Epoll.isAvailable();

    }

    public Class<? extends ServerChannel> getServerChannelClazz() {
        return SERVER_CHANNEL_CLAZZ;
    }

    /**
     * Gets server socket send buf size.
     *
     * @return the server socket send buf size
     */
    public int getServerSocketSendBufSize() {
        return serverSocketSendBufSize;
    }

    /**
     * Sets server socket send buf size.
     *
     * @param serverSocketSendBufSize the server socket send buf size
     */
    public void setServerSocketSendBufSize(int serverSocketSendBufSize) {
        this.serverSocketSendBufSize = serverSocketSendBufSize;
    }

    /**
     * Gets server socket resv buf size.
     *
     * @return the server socket resv buf size
     */
    public int getServerSocketResvBufSize() {
        return serverSocketResvBufSize;
    }

    /**
     * Sets server socket resv buf size.
     *
     * @param serverSocketResvBufSize the server socket resv buf size
     */
    public void setServerSocketResvBufSize(int serverSocketResvBufSize) {
        this.serverSocketResvBufSize = serverSocketResvBufSize;
    }

    /**
     * Gets server worker threads.
     *
     * @return the server worker threads
     */
    public int getServerWorkerThreads() {
        return serverWorkerThreads;
    }

    /**
     * Sets server worker threads.
     *
     * @param serverWorkerThreads the server worker threads
     */
    public void setServerWorkerThreads(int serverWorkerThreads) {
        this.serverWorkerThreads = serverWorkerThreads;
    }

    /**
     * Gets so back log size.
     *
     * @return the so back log size
     */
    public int getSoBackLogSize() {
        return soBackLogSize;
    }

    /**
     * Sets so back log size.
     *
     * @param soBackLogSize the so back log size
     */
    public void setSoBackLogSize(int soBackLogSize) {
        this.soBackLogSize = soBackLogSize;
    }

    /**
     * Gets write buffer high water mark.
     *
     * @return the write buffer high water mark
     */
    public int getWriteBufferHighWaterMark() {
        return writeBufferHighWaterMark;
    }

    /**
     * Sets write buffer high water mark.
     *
     * @param writeBufferHighWaterMark the write buffer high water mark
     */
    public void setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    }

    /**
     * Gets write buffer low water mark.
     *
     * @return the write buffer low water mark
     */
    public int getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    /**
     * Sets write buffer low water mark.
     *
     * @param writeBufferLowWaterMark the write buffer low water mark
     */
    public void setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }

    /**
     * Gets listen port.
     *
     * @return the listen port
     */
    public int getDefaultListenPort() {
        return DEFAULT_LISTEN_PORT;
    }

    /**
     * Gets channel max read idle seconds.
     *
     * @return the channel max read idle seconds
     */
    public int getChannelMaxReadIdleSeconds() {
        return MAX_READ_IDLE_SECONDS;
    }

    /**
     * Gets server channel max idle time seconds.
     *
     * @return the server channel max idle time seconds
     */
    public int getServerChannelMaxIdleTimeSeconds() {
        return serverChannelMaxIdleTimeSeconds;
    }

    /**
     * Gets rpc request timeout.
     *
     * @return the rpc request timeout
     */
    public static int getRpcRequestTimeout() {
        return RPC_REQUEST_TIMEOUT;
    }

    /**
     * Get boss thread prefix string.
     *
     * @return the string
     */
    public String getBossThreadPrefix() {
        return StringUtils.isBlank(transportProperties.getThreadFactory().getBossThreadPrefix())
                ? DEFAULT_BOSS_THREAD_PREFIX
                : transportProperties.getThreadFactory().getBossThreadPrefix();
    }

    /**
     * Get worker thread prefix string.
     *
     * @return the string
     */
    public String getWorkerThreadPrefix() {
        return StringUtils.isBlank(transportProperties.getThreadFactory().getWorkerThreadPrefix())
                ? enableEpoll() ? EPOLL_WORKER_THREAD_PREFIX : DEFAULT_NIO_WORKER_THREAD_PREFIX
                : transportProperties.getThreadFactory().getWorkerThreadPrefix();
    }

    /**
     * Get executor thread prefix string.
     *
     * @return the string
     */
    public String getExecutorThreadPrefix() {
        return StringUtils.isBlank(transportProperties.getThreadFactory().getServerExecutorThreadPrefix())
                ? DEFAULT_EXECUTOR_THREAD_PREFIX
                : transportProperties.getThreadFactory().getServerExecutorThreadPrefix();
    }

    /**
     * Get boss thread size int.
     *
     * @return the int
     */
    public int getBossThreadSize() {
        return transportProperties.getThreadFactory().getBossThreadSize() == 0
                ? DEFAULT_BOSS_THREAD_SIZE
                : transportProperties.getThreadFactory().getBossThreadSize();
    }

    /**
     * Get the timeout seconds of shutdown.
     *
     * @return the int
     */
    public int getServerShutdownWaitTime() {
        return DEFAULT_SHUTDOWN_TIMEOUT_SEC;
    }
}
