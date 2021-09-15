package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 11:06
 */
@Slf4j
public class GuavaEventBus implements EventBus {

    private final com.google.common.eventbus.EventBus eventBus;

    public GuavaEventBus(String identifier) {
        this(identifier, false);
    }

    public GuavaEventBus(String identifier, boolean async) {
        if (!async) {
            this.eventBus = new com.google.common.eventbus.EventBus(identifier);
        } else {
            final ExecutorService eventExecutor = new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(2048), new NamedThreadFactory(identifier, 1), (r, executor) -> {

                log.warn("eventBus executor queue is full, size:{}", executor.getQueue().size());
            });
            this.eventBus = new com.google.common.eventbus.AsyncEventBus(identifier, eventExecutor);
        }
    }

    @Override
    public void register(Object subscriber) {
        this.eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        this.eventBus.unregister(subscriber);
    }

    @Override
    public void post(Event event) {
        this.eventBus.post(event);
    }
}
