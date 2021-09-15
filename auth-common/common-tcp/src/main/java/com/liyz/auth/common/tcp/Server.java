package com.liyz.auth.common.tcp;

import com.liyz.auth.common.tcp.core.DefaultCoordinator;
import com.liyz.auth.common.tcp.core.ShutdownHook;
import com.liyz.auth.common.tcp.core.rpc.NettyRemotingServer;
import com.liyz.auth.common.tcp.thread.NamedThreadFactory;
import com.liyz.auth.common.tcp.v1.properties.TransportProperties;
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
 * @date 2021/9/14 11:17
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({TransportProperties.class})
public class Server implements ApplicationListener<ApplicationStartedEvent>, Runnable {

    private static final int MIN_SERVER_POOL_SIZE = 2;
    private static final int MAX_SERVER_POOL_SIZE = 5;
    private static final int MAX_TASK_QUEUE_SIZE = 20000;
    private static final int KEEP_ALIVE_TIME = 500;
    private static final ThreadPoolExecutor WORKING_THREADS = new ThreadPoolExecutor(MIN_SERVER_POOL_SIZE,
            MAX_SERVER_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(MAX_TASK_QUEUE_SIZE),
            new NamedThreadFactory("ServerHandlerThread", MAX_SERVER_POOL_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());

    private TransportProperties transportProperties;

    public Server(TransportProperties transportProperties) {
        this.transportProperties = transportProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        new Thread(this).start();
    }

    @Override
    public void run() {

        NettyRemotingServer nettyRemotingServer = new NettyRemotingServer(WORKING_THREADS, transportProperties);
        //server port
        nettyRemotingServer.setListenPort(transportProperties.getPort());
        DefaultCoordinator coordinator = new DefaultCoordinator(nettyRemotingServer);
        coordinator.init();
        nettyRemotingServer.setHandler(coordinator);
        // register ShutdownHook
        ShutdownHook.getInstance().addDisposable(coordinator);
        ShutdownHook.getInstance().addDisposable(nettyRemotingServer);
        try {
            nettyRemotingServer.init();
        } catch (Throwable e) {
            log.error("nettyServer init error:{}", e.getMessage(), e);
            System.exit(-1);
        }

        System.exit(0);
    }
}
