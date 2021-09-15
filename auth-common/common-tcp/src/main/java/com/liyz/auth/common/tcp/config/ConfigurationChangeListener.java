package com.liyz.auth.common.tcp.config;

import com.liyz.auth.common.tcp.thread.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 16:50
 */
public interface ConfigurationChangeListener {

    /**
     * The constant CORE_LISTENER_THREAD.
     */
    int CORE_LISTENER_THREAD = 1;
    /**
     * The constant MAX_LISTENER_THREAD.
     */
    int MAX_LISTENER_THREAD = 1;
    /**
     * The constant EXECUTOR_SERVICE.
     */
    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(CORE_LISTENER_THREAD, MAX_LISTENER_THREAD,
            Integer.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            new NamedThreadFactory("configListenerOperate", MAX_LISTENER_THREAD));

    /**
     * Process.
     *
     * @param event the event
     */
    void onChangeEvent(ConfigurationChangeEvent event);

    /**
     * On process event.
     *
     * @param event the event
     */
    default void onProcessEvent(ConfigurationChangeEvent event) {
        getExecutorService().submit(() -> {
            beforeEvent();
            onChangeEvent(event);
            afterEvent();
        });
    }

    /**
     * On shut down.
     */
    default void onShutDown() {
        getExecutorService().shutdownNow();
    }

    /**
     * Gets executor service.
     *
     * @return the executor service
     */
    default ExecutorService getExecutorService() {
        return EXECUTOR_SERVICE;
    }

    /**
     * Before event.
     */
    default void beforeEvent() {

    }

    /**
     * After event.
     */
    default void afterEvent() {

    }
}
