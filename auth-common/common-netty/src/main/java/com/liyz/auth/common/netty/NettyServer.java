package com.liyz.auth.common.netty;

import com.liyz.auth.common.netty.properties.NettyServerProperties;
import com.liyz.auth.common.netty.rpc.NettyRemotingServer;
import com.liyz.auth.common.netty.thread.NamedThreadFactory;
import com.liyz.auth.common.netty.thread.ShutdownHook;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2021/9/15 11:20
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({NettyServerProperties.class})
public class NettyServer implements ApplicationListener<ApplicationStartedEvent>, Runnable{

    private static final int MIN_SERVER_POOL_SIZE = 2;
    private static final int MAX_SERVER_POOL_SIZE = 5;
    private static final int MAX_TASK_QUEUE_SIZE = 200;
    private static final int KEEP_ALIVE_TIME = 500;
    private static final ThreadPoolExecutor WORKING_THREADS = new ThreadPoolExecutor(1,
            1, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2),
            new NamedThreadFactory("ServerHandlerThread", 1), new ThreadPoolExecutor.CallerRunsPolicy());

    private NettyServerProperties nettyServerProperties;
    private ThreadPoolExecutor nettyExecutor = new ThreadPoolExecutor(
            MIN_SERVER_POOL_SIZE,
            MAX_SERVER_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(MAX_TASK_QUEUE_SIZE),
            new NamedThreadFactory("NettyServerThread", MAX_SERVER_POOL_SIZE),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public NettyServer(NettyServerProperties nettyServerProperties) {
        this.nettyServerProperties = nettyServerProperties;
    }


    @Override
    public void run() {
        NettyRemotingServer nettyRemotingServer = new NettyRemotingServer(WORKING_THREADS, nettyServerProperties);
        //server port
        nettyRemotingServer.setListenPort(nettyServerProperties.getPort());
        // register ShutdownHook
        ShutdownHook.getInstance().addDisposable(nettyRemotingServer);
        try {
            nettyRemotingServer.init();
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
