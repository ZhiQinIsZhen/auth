package com.liyz.auth.common.websocket.config;

import com.liyz.auth.common.websocket.core.WebsocketRemotingServer;
import com.liyz.auth.common.websocket.thread.NamedThreadFactory;
import com.liyz.auth.common.websocket.thread.ShutdownHook;
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
 * @date 2021/9/26 10:30
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({WebsocketProperties.class})
@ConditionalOnProperty(prefix = "spring.websocket", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
public class WebsocketServerAutoConfig implements ApplicationListener<ApplicationStartedEvent>, Runnable{

    private static final int MIN_SERVER_POOL_SIZE = 2;
    private static final int MAX_SERVER_POOL_SIZE = 5;
    private static final int MAX_TASK_QUEUE_SIZE = 200;
    private static final int KEEP_ALIVE_TIME = 500;
    private static final ThreadPoolExecutor WORKING_THREADS = new ThreadPoolExecutor(MIN_SERVER_POOL_SIZE,
            MAX_SERVER_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(MAX_TASK_QUEUE_SIZE),
            new NamedThreadFactory("ServerHandlerThread", MAX_SERVER_POOL_SIZE),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private static final String START_EXECUTOR_PREFIX = "WebsocketStartThread";
    private ThreadPoolExecutor websocketStartExecutor = new ThreadPoolExecutor(
            1,
            1, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(20),
            new NamedThreadFactory(START_EXECUTOR_PREFIX, 1),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private WebsocketServerConfig websocketServerConfig;

    public WebsocketServerAutoConfig(WebsocketProperties websocketProperties) {
        this.websocketServerConfig = new WebsocketServerConfig(websocketProperties);
    }

    @Override
    public void run() {
        WebsocketRemotingServer websocketRemotingServer = new WebsocketRemotingServer(WORKING_THREADS, websocketServerConfig);
        //server port
        websocketRemotingServer.setListenPort(websocketRemotingServer.getListenPort());
        // register ShutdownHook
        ShutdownHook.getInstance().addDisposable(websocketRemotingServer);
        try {
            websocketRemotingServer.init();
        } catch (Throwable e) {
            log.error("nettyServer init error:{}", e.getMessage(), e);
            System.exit(-1);
        }

        System.exit(0);
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        websocketStartExecutor.execute(this);
    }
}
