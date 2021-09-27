package com.liyz.auth.common.websocket.core;

import com.liyz.auth.common.websocket.config.WebsocketServerConfig;
import com.liyz.auth.common.websocket.thread.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
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
 * @date 2021/9/26 14:56
 */
@Slf4j
public class WebsocketBootstrap implements RemotingBootstrap {

    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    private final EventLoopGroup eventLoopGroupWorker;
    private final EventLoopGroup eventLoopGroupBoss;
    private ChannelHandler[] channelHandlers;
    @Getter
    private final WebsocketServerConfig websocketServerConfig;
    @Getter
    private int listenPort;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public WebsocketBootstrap(WebsocketServerConfig websocketServerConfig) {
        this.websocketServerConfig = websocketServerConfig;
        if (websocketServerConfig.enableEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(websocketServerConfig.getBossThreadSize(),
                    new NamedThreadFactory(websocketServerConfig.getBossThreadPrefix(), websocketServerConfig.getBossThreadSize()));
            this.eventLoopGroupWorker = new EpollEventLoopGroup(websocketServerConfig.getServerWorkerThreads(),
                    new NamedThreadFactory(websocketServerConfig.getWorkerThreadPrefix(),
                            websocketServerConfig.getServerWorkerThreads()));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(websocketServerConfig.getBossThreadSize(),
                    new NamedThreadFactory(websocketServerConfig.getBossThreadPrefix(), websocketServerConfig.getBossThreadSize()));
            this.eventLoopGroupWorker = new NioEventLoopGroup(websocketServerConfig.getServerWorkerThreads(),
                    new NamedThreadFactory(websocketServerConfig.getWorkerThreadPrefix(),
                            websocketServerConfig.getServerWorkerThreads()));
        }
        if (listenPort == 0) {
            setListenPort(websocketServerConfig.getSocketPort());
        }
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
     * Sets listen port.
     *
     * @param listenPort the listen port
     */
    public void setListenPort(int listenPort) {
        if (listenPort <= 0) {
            throw new IllegalArgumentException("listen port: " + listenPort + " is invalid!");
        }
        this.listenPort = listenPort;
    }

    @Override
    public void start() {
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupWorker)
                .channel(websocketServerConfig.getServerChannelClazz())
                //处理线程全满时，临时缓存的请求个数
                .option(ChannelOption.SO_BACKLOG, websocketServerConfig.getSoBackLogSize())
                //地址复用
                .option(ChannelOption.SO_REUSEADDR, true)
                //探测客户端存活性
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //低延时多交互次数
                .childOption(ChannelOption.TCP_NODELAY, true)
                //发送缓冲区
                .childOption(ChannelOption.SO_SNDBUF, websocketServerConfig.getServerSocketSendBufSize())
                //接收缓冲区
                .childOption(ChannelOption.SO_RCVBUF, websocketServerConfig.getServerSocketResvBufSize())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(websocketServerConfig.getWriteBufferLowWaterMark(),
                                websocketServerConfig.getWriteBufferHighWaterMark()))
                .localAddress(new InetSocketAddress(listenPort))
                .childHandler(new SocketChannelInitializer(
                        websocketServerConfig.getSocketPath(),
                        websocketServerConfig.getMaxReadIdleSeconds(),
                        websocketServerConfig.getMaxWriteIdleSeconds(),
                        websocketServerConfig.getMaxReadIdleSeconds(),
                        channelHandlers)
                );

        try {
            ChannelFuture future = this.serverBootstrap.bind(listenPort).sync();
            log.info("Server started, listen port: {}", listenPort);
            initialized.set(true);
            future.channel().closeFuture().sync();
        } catch (Exception exx) {
            throw new RuntimeException(exx);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.info("Shutting server down. ");
            if (initialized.get()) {
                //wait a few seconds for server transport
                TimeUnit.SECONDS.sleep(websocketServerConfig.getServerShutdownWaitTime());
            }

            this.eventLoopGroupBoss.shutdownGracefully();
            this.eventLoopGroupWorker.shutdownGracefully();
        } catch (Exception exx) {
            log.error(exx.getMessage());
        }
    }
}
