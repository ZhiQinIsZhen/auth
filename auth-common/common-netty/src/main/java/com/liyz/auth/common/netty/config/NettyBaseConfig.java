package com.liyz.auth.common.netty.config;

import com.liyz.auth.common.netty.constant.NettyProtocolType;
import com.liyz.auth.common.netty.constant.NettyServerType;
import com.liyz.auth.common.netty.properties.NettyServerProperties;
import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.PlatformDependent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 15:54
 */
@Slf4j
public abstract class NettyBaseConfig {

    private static final int DEFAULT_WRITE_IDLE_SECONDS = 5;
    private static final int READIDLE_BASE_WRITEIDLE = 3;
    private static final int DEFAULT_LISTEN_PORT = 8091;

    protected final NettyServerProperties nettyServerProperties;
    protected final NettyProtocolType nettyProtocolType;
    protected final NettyServerType nettyServerType;
    protected int workerThreadSize;
    @Getter
    protected final Class<? extends ServerChannel> serverChannelClazz;
    @Getter
    protected final Class<? extends Channel> clientChannelClazz;
    protected final int maxWriteIdleSeconds;
    @Getter
    protected final int maxReadIdleSeconds;

    public NettyBaseConfig(NettyServerProperties nettyServerProperties) {
        this.nettyServerProperties = nettyServerProperties;
        this.nettyProtocolType = NettyProtocolType.getType(nettyServerProperties.getType());
        this.nettyServerType = NettyServerType.getType(nettyServerProperties.getServer());
        String workerThreadSize = nettyServerProperties.getThreadFactory().getWorkerThreadSize();
        if (StringUtils.isNotBlank(workerThreadSize) && StringUtils.isNumeric(workerThreadSize)) {
            this.workerThreadSize = Integer.parseInt(workerThreadSize);
        } else if (NettyServerConfig.WorkThreadMode.getModeByName(workerThreadSize) != null) {
            this.workerThreadSize = NettyServerConfig.WorkThreadMode.getModeByName(workerThreadSize).getValue();
        } else {
            this.workerThreadSize = NettyServerConfig.WorkThreadMode.Default.getValue();
        }
        switch (nettyServerType) {
            case NIO:
                if (nettyProtocolType == NettyProtocolType.TCP) {
                    serverChannelClazz = NioServerSocketChannel.class;
                    clientChannelClazz = NioSocketChannel.class;
                } else {
                    raiseUnsupportedTransportError();
                    serverChannelClazz = null;
                    clientChannelClazz = null;
                }
                break;
            case NATIVE:
                if (PlatformDependent.isWindows()) {
                    throw new IllegalArgumentException("no native supporting for Windows.");
                } else if (PlatformDependent.isOsx()) {
                    if (nettyProtocolType == NettyProtocolType.TCP) {
                        serverChannelClazz = KQueueServerSocketChannel.class;
                        clientChannelClazz = KQueueSocketChannel.class;
                    } else if (nettyProtocolType == NettyProtocolType.UNIX_DOMAIN_SOCKET) {
                        serverChannelClazz = KQueueServerDomainSocketChannel.class;
                        clientChannelClazz = KQueueDomainSocketChannel.class;
                    } else {
                        raiseUnsupportedTransportError();
                        serverChannelClazz = null;
                        clientChannelClazz = null;
                    }
                } else {
                    if (nettyProtocolType == NettyProtocolType.TCP) {
                        serverChannelClazz = EpollServerSocketChannel.class;
                        clientChannelClazz = EpollSocketChannel.class;
                    } else if (nettyProtocolType == NettyProtocolType.UNIX_DOMAIN_SOCKET) {
                        serverChannelClazz = EpollServerDomainSocketChannel.class;
                        clientChannelClazz = EpollDomainSocketChannel.class;
                    } else {
                        raiseUnsupportedTransportError();
                        serverChannelClazz = null;
                        clientChannelClazz = null;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("unsupported.");
        }
        boolean enableHeartbeat = nettyServerProperties.isHeartbeat();
        if (enableHeartbeat) {
            maxWriteIdleSeconds = DEFAULT_WRITE_IDLE_SECONDS;
        } else {
            maxWriteIdleSeconds = 0;
        }
        maxReadIdleSeconds = maxWriteIdleSeconds * READIDLE_BASE_WRITEIDLE;
    }

    private void raiseUnsupportedTransportError() {
        String errMsg = String.format("Unsupported provider type :[%s] for transport:[%s].", nettyServerProperties.getServer(),
                nettyServerProperties.getType());
        log.error(errMsg);
        throw new IllegalArgumentException(errMsg);
    }
}
