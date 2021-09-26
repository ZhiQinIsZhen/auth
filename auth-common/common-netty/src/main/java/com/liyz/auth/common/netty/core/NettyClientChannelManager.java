package com.liyz.auth.common.netty.core;

import com.liyz.auth.common.netty.rpc.NettyClientBootstrap;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 16:33
 */
@Slf4j
public class NettyClientChannelManager {

    public NettyClientChannelManager(NettyClientBootstrap clientBootstrap) {
        this.clientBootstrap = clientBootstrap;
    }

    private final NettyClientBootstrap clientBootstrap;

    private final ConcurrentMap<String, Object> channelLocks = new ConcurrentHashMap<>();

    @Getter
    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    /**
     * Acquire netty client channel connected to remote server.
     *
     * @param serverAddress server address
     * @return netty channel
     */
    public Channel acquireChannel(String serverAddress) {
        Channel channelToServer = channels.get(serverAddress);
        if (channelToServer != null) {
            return channelToServer;
        }
        log.info("will connect to " + serverAddress);
        Object lockObj = channelLocks.computeIfAbsent(serverAddress, key -> new Object());
        synchronized (lockObj) {
            return doConnect(serverAddress);
        }
    }

    /**
     * Release channel to pool if necessary.
     *
     * @param channel channel
     * @param serverAddress server address
     */
    public void releaseChannel(Channel channel, String serverAddress) {
        if (channel == null || serverAddress == null) { return; }
        try {
            synchronized (channelLocks.get(serverAddress)) {
                Channel ch = channels.get(serverAddress);
                if (ch == null) {
//                    nettyClientKeyPool.returnObject(poolKeyMap.get(serverAddress), channel);
                    return;
                }
                if (ch.compareTo(channel) == 0) {
                    log.info("return to pool, rm channel:{}", channel);
                    destroyChannel(serverAddress, channel);
                } else {
//                    nettyClientKeyPool.returnObject(poolKeyMap.get(serverAddress), channel);
                }
            }
        } catch (Exception exx) {
            log.error(exx.getMessage());
        }
    }

    /**
     * Destroy channel.
     *
     * @param serverAddress server address
     * @param channel channel
     */
    public void destroyChannel(String serverAddress, Channel channel) {
        if (channel == null) { return; }
        try {
            if (channel.equals(channels.get(serverAddress))) {
                channels.remove(serverAddress);
            }
//            nettyClientKeyPool.returnObject(poolKeyMap.get(serverAddress), channel);
        } catch (Exception exx) {
            log.error("return channel to rmPool error:{}", exx.getMessage());
        }
    }

    /**
     * Reconnect to remote server of current transaction service group.
     *
     */
   public void reconnect(List<String> availList) {
        for (String serverAddress : availList) {
            try {
                acquireChannel(serverAddress);
            } catch (Exception e) {
                log.error("an not connect to {} cause:{}", serverAddress, e.getMessage(), e);
            }
        }
    }

    public void invalidateObject(final String serverAddress, final Channel channel) throws Exception {
//        nettyClientKeyPool.invalidateObject(poolKeyMap.get(serverAddress), channel);
    }

    public void registerChannel(final String serverAddress, final Channel channel) {
        Channel channelToServer = channels.get(serverAddress);
        if (channelToServer != null && channelToServer.isActive()) {
            return;
        }
        channels.put(serverAddress, channel);
    }

    private Channel doConnect(String serverAddress) {
        Channel channelToServer = channels.get(serverAddress);
        if (channelToServer != null && channelToServer.isActive()) {
            return channelToServer;
        }
        Channel channelFromPool = null;
        try {
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8099);
            channelFromPool = clientBootstrap.getNewChannel(address);
            channels.put(serverAddress, channelFromPool);
        } catch (Exception exx) {
            log.error("{} register RM failed.", exx);
            throw new RemoteServiceException("can not register RM,err:" + exx.getMessage());
        }
        return channelFromPool;
    }
}
