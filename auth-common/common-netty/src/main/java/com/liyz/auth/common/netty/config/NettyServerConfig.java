package com.liyz.auth.common.netty.config;

import com.liyz.auth.common.netty.constant.NettyProtocolType;
import com.liyz.auth.common.netty.constant.NettyServerConstant;
import com.liyz.auth.common.netty.constant.NettyServerType;
import com.liyz.auth.common.netty.properties.NettyServerProperties;
import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.KQueueDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.PlatformDependent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 16:38
 */
@Slf4j
public class NettyServerConfig extends NettyBaseConfig {


    private static final String EPOLL_WORKER_THREAD_PREFIX = "NettyServerEPollWorker";
    private static final int RPC_REQUEST_TIMEOUT = 30 * 1000;

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
    private int writeBufferHighWaterMark = 67108864;
    @Getter
    @Setter
    private int writeBufferLowWaterMark = 1048576;
    private int serverChannelMaxIdleTimeSeconds = 30;

    public NettyServerConfig(NettyServerProperties nettyServerProperties) {
        super(nettyServerProperties);
    }



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
        return Objects.isNull(nettyServerProperties.getThreadFactory().getBossThreadSize()) ?
                NettyServerConstant.DEFAULT_BOSS_THREAD_SIZE : nettyServerProperties.getThreadFactory().getBossThreadSize();
    }

    /**
     * Get boss thread prefix string.
     *
     * @return the string
     */
    public String getBossThreadPrefix() {
        return Objects.isNull(nettyServerProperties.getThreadFactory().getBossThreadPrefix()) ?
                NettyServerConstant.DEFAULT_BOSS_THREAD_PREFIX : nettyServerProperties.getThreadFactory().getBossThreadPrefix();
    }

    /**
     * Get worker thread prefix string.
     *
     * @return the string
     */
    public String getWorkerThreadPrefix() {
        return Objects.isNull(nettyServerProperties.getThreadFactory().getWorkerThreadPrefix()) ? enableEpoll()
                ? EPOLL_WORKER_THREAD_PREFIX : NettyServerConstant.DEFAULT_NIO_WORKER_THREAD_PREFIX
                : nettyServerProperties.getThreadFactory().getWorkerThreadPrefix();
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
     * Get the timeout seconds of shutdown.
     *
     * @return the int
     */
    public int getServerShutdownWaitTime() {
        return nettyServerProperties.getShutdownWait() == 0
                ? NettyServerConstant.DEFAULT_SHUTDOWN_TIMEOUT_SEC : nettyServerProperties.getShutdownWait();
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
     * The enum Work thread mode.
     */
    public enum WorkThreadMode {

        /**
         * Auto work thread mode.
         */
        Auto(NettyRuntime.availableProcessors() * 2 + 1),
        /**
         * Pin work thread mode.
         */
        Pin(NettyRuntime.availableProcessors()),
        /**
         * Busy pin work thread mode.
         */
        BusyPin(NettyRuntime.availableProcessors() + 1),
        /**
         * Default work thread mode.
         */
        Default(NettyRuntime.availableProcessors() * 2);

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        private int value;

        WorkThreadMode(int value) {
            this.value = value;
        }

        /**
         * Gets mode by name.
         *
         * @param name the name
         * @return the mode by name
         */
        public static WorkThreadMode getModeByName(String name) {
            for (WorkThreadMode mode : values()) {
                if (mode.name().equalsIgnoreCase(name)) {
                    return mode;
                }
            }
            return null;
        }
    }
}
