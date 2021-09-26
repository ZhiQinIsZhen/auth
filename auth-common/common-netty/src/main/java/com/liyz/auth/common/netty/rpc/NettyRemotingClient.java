package com.liyz.auth.common.netty.rpc;

import com.liyz.auth.common.netty.properties.NettyServerProperties;
import com.liyz.auth.common.netty.protocol.AbstractMessage;
import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 17:05
 */
public class NettyRemotingClient extends AbstractNettyRemotingClient{


    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public NettyRemotingClient(NettyServerProperties nettyServerProperties, EventExecutorGroup eventExecutorGroup, ThreadPoolExecutor messageExecutor) {
        super(nettyServerProperties, eventExecutorGroup, messageExecutor);
    }

    @Override
    public void onRegisterMsgSuccess(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage) {

    }

    @Override
    public void onRegisterMsgFail(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage) {

    }
}
