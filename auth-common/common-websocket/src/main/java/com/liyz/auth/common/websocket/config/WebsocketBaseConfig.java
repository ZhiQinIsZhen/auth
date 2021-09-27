package com.liyz.auth.common.websocket.config;

import com.liyz.auth.common.websocket.constant.WebsocketType;
import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerDomainSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.PlatformDependent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 10:48
 */
@Slf4j
public abstract class WebsocketBaseConfig {

    private static final int DEFAULT_WRITE_IDLE_SECONDS = 5;
    private static final int READIDLE_BASE_WRITEIDLE = 3;
    @Getter
    protected final Class<? extends ServerChannel> serverChannelClazz;
    @Getter
    protected final Class<? extends Channel> clientChannelClazz;

    protected final  WebsocketProperties websocketProperties;
    protected final WebsocketType websocketType;
    protected final int workerThreadSize;
    @Getter
    protected final int maxWriteIdleSeconds;
    @Getter
    protected final int maxReadIdleSeconds;

    public WebsocketBaseConfig(WebsocketProperties websocketProperties) {
        this.websocketProperties = websocketProperties;
        this.websocketType = WebsocketType.getType(websocketProperties.getType());
        String workerThreadSize = websocketProperties.getThreadFactory().getWorkerThreadSize();
        if (StringUtils.isNotBlank(workerThreadSize) && StringUtils.isNumeric(workerThreadSize)) {
            this.workerThreadSize = Integer.parseInt(workerThreadSize);
        } else if (Objects.nonNull(WorkThreadMode.getModeByName(workerThreadSize))) {
            this.workerThreadSize = WorkThreadMode.getModeByName(workerThreadSize).getValue();
        } else {
            this.workerThreadSize = WorkThreadMode.Default.getValue();
        }
        switch (websocketType) {
            case NIO:
                serverChannelClazz = NioServerSocketChannel.class;
                clientChannelClazz = NioSocketChannel.class;
                break;
            case NATIVE:
                if (PlatformDependent.isWindows()) {
                    throw new IllegalArgumentException("no native supporting for Windows.");
                } else if (PlatformDependent.isOsx()) {
                    serverChannelClazz = KQueueServerDomainSocketChannel.class;
                    clientChannelClazz = KQueueDomainSocketChannel.class;
                    /*serverChannelClazz = KQueueServerSocketChannel.class;
                    clientChannelClazz = KQueueSocketChannel.class;*/
                } else {
                    serverChannelClazz = EpollServerSocketChannel.class;
                    clientChannelClazz = EpollSocketChannel.class;
                    /*serverChannelClazz = EpollServerDomainSocketChannel.class;
                    clientChannelClazz = EpollDomainSocketChannel.class;*/
                }
                break;
            default:
                throw new IllegalArgumentException("unsupported.");
        }
        boolean enableHeartbeat = websocketProperties.isHeartbeat();
        if (enableHeartbeat) {
            maxWriteIdleSeconds = DEFAULT_WRITE_IDLE_SECONDS;
        } else {
            maxWriteIdleSeconds = 0;
        }
        maxReadIdleSeconds = maxWriteIdleSeconds * READIDLE_BASE_WRITEIDLE;
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
