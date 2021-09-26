package com.liyz.auth.common.netty;

import com.liyz.auth.common.netty.properties.NettyServerProperties;
import com.liyz.auth.common.netty.rpc.NettyRemotingClient;
import com.liyz.auth.common.netty.thread.NamedThreadFactory;
import com.liyz.auth.common.netty.thread.RejectedPolicies;
import com.liyz.auth.common.netty.thread.ShutdownHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 15:30
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "netty.server", name = {"enabled"}, havingValue = "false")
@EnableConfigurationProperties({NettyServerProperties.class})
public class NettyClient implements ApplicationListener<ApplicationStartedEvent>, Runnable{



    private NettyServerProperties nettyServerProperties;
    private ThreadPoolExecutor nettyExecutor = new ThreadPoolExecutor(
            1,
            1, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2),
            new NamedThreadFactory("NettyServerThread", 1),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private final ThreadPoolExecutor messageExecutor;


    public NettyClient(NettyServerProperties nettyServerProperties) {
        this.nettyServerProperties = nettyServerProperties;
        messageExecutor = new ThreadPoolExecutor(
                nettyServerProperties.getThreadFactory().getBossThreadSize(), nettyServerProperties.getThreadFactory().getBossThreadSize(),
                Integer.MAX_VALUE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200),
                new NamedThreadFactory(nettyServerProperties.getThreadFactory().getClientWorkerThreadPrefix(),
                        nettyServerProperties.getThreadFactory().getBossThreadSize()),
                RejectedPolicies.runsOldestTaskPolicy());

    }

    @Override
    public void run() {
        NettyRemotingClient nettyRemotingClient = new NettyRemotingClient(nettyServerProperties, null, messageExecutor);
        // register ShutdownHook
        ShutdownHook.getInstance().addDisposable(nettyRemotingClient);
        try {
            nettyRemotingClient.init();
        } catch (Throwable e) {
            log.error("nettyServer init error:{}", e.getMessage(), e);
            System.exit(-1);
        }

        System.exit(0);
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        nettyExecutor.execute(this);
    }
}
