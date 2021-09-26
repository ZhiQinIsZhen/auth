package com.liyz.auth.common.netty.rpc;

import com.liyz.auth.common.netty.config.NettyClientConfig;
import com.liyz.auth.common.netty.core.v1.ProtocolV1Decoder;
import com.liyz.auth.common.netty.core.v1.ProtocolV1Encoder;
import com.liyz.auth.common.netty.properties.NettyServerProperties;
import com.liyz.auth.common.netty.thread.NamedThreadFactory;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollMode;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.PlatformDependent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 15:40
 */
@Slf4j
public class NettyClientBootstrap implements RemotingBootstrap{

    @Getter
    private final NettyClientConfig nettyClientConfig;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private ChannelHandler[] channelHandlers;
    private final EventLoopGroup eventLoopGroupWorker;
    private EventExecutorGroup defaultEventExecutorGroup;
    private final Bootstrap bootstrap = new Bootstrap();

    public NettyClientBootstrap(NettyServerProperties nettyServerProperties, final EventExecutorGroup eventExecutorGroup) {
        this.nettyClientConfig = new NettyClientConfig(nettyServerProperties);
        int selectorThreadSizeThreadSize = nettyServerProperties.getThreadFactory().getClientSelectorThreadSize();
        this.eventLoopGroupWorker = new NioEventLoopGroup(selectorThreadSizeThreadSize,
                new NamedThreadFactory(getThreadPrefix(this.nettyClientConfig.getClientSelectorThreadPrefix()),
                        selectorThreadSizeThreadSize));
        this.defaultEventExecutorGroup = eventExecutorGroup;
    }

    private String getThreadPrefix(String threadPrefix) {
        return threadPrefix + "_" + "client";
    }

    /**
     * Sets channel handlers.
     *
     * @param handlers the handlers
     */
    protected void setChannelHandlers(final ChannelHandler... handlers) {
        if (handlers != null) {
            channelHandlers = handlers;
        }
    }

    /**
     * Add channel pipeline last.
     *
     * @param channel  the channel
     * @param handlers the handlers
     */
    private void addChannelPipelineLast(Channel channel, ChannelHandler... handlers) {
        if (channel != null && handlers != null) {
            channel.pipeline().addLast(handlers);
        }
    }

    @Override
    public void start() {
        if (this.defaultEventExecutorGroup == null) {
            this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(nettyClientConfig.getClientWorkerThreads(),
                    new NamedThreadFactory(getThreadPrefix(nettyClientConfig.getClientWorkerThreadPrefix()),
                            nettyClientConfig.getClientWorkerThreads()));
        }
        this.bootstrap.group(this.eventLoopGroupWorker).channel(
                nettyClientConfig.getClientChannelClazz()).option(
                ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis()).option(
                ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize()).option(ChannelOption.SO_RCVBUF,
                nettyClientConfig.getClientSocketRcvBufSize());

        if (nettyClientConfig.enableNative()) {
            if (PlatformDependent.isOsx()) {
                log.info("client run on macOS");
            } else {
                bootstrap.option(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED)
                        .option(EpollChannelOption.TCP_QUICKACK, true);
            }
        }

        bootstrap.handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                new IdleStateHandler(nettyClientConfig.getChannelMaxReadIdleSeconds(),
                                        nettyClientConfig.getChannelMaxWriteIdleSeconds(),
                                        nettyClientConfig.getChannelMaxAllIdleSeconds()))
                                .addLast(new ProtocolV1Decoder())
                                .addLast(new ProtocolV1Encoder());
                        if (channelHandlers != null) {
                            addChannelPipelineLast(ch, channelHandlers);
                        }
                    }
                });

        if (initialized.compareAndSet(false, true) && log.isInfoEnabled()) {
            log.info("NettyClientBootstrap has started");
        }
    }

    @Override
    public void shutdown() {
        try {
            this.eventLoopGroupWorker.shutdownGracefully();
            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception exx) {
            log.error("Failed to shutdown: {}", exx.getMessage());
        }
    }

    /**
     * Gets new channel.
     *
     * @param address the address
     * @return the new channel
     */
    public Channel getNewChannel(InetSocketAddress address) {
        Channel channel;
        ChannelFuture f = this.bootstrap.connect(address);
        try {
            f.await(this.nettyClientConfig.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS);
            if (f.isCancelled()) {
                throw new RemoteServiceException("connect cancelled, can not connect to services-server.", f.cause());
            } else if (!f.isSuccess()) {
                throw new RemoteServiceException("connect failed, can not connect to services-server.", f.cause());
            } else {
                channel = f.channel();
            }
        } catch (Exception e) {
            throw new RemoteServiceException("can not connect to services-server.", e);
        }
        return channel;
    }
}
