package com.liyz.auth.common.websocket.thread;

import com.liyz.auth.common.websocket.core.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/27 14:00
 */
@Slf4j
public class ShutdownHook extends Thread{

    private static final ShutdownHook SHUTDOWN_HOOK = new ShutdownHook("ShutdownHook");

    private final PriorityQueue<DisposablePriorityWrapper> disposables = new PriorityQueue<>();

    private final AtomicBoolean destroyed = new AtomicBoolean(false);

    /**
     * default 10. Lower values have higher priority
     */
    private static final int DEFAULT_PRIORITY = 10;

    static {
        Runtime.getRuntime().addShutdownHook(SHUTDOWN_HOOK);
    }

    private ShutdownHook(String name) {
        super(name);
    }

    public static ShutdownHook getInstance() {
        return SHUTDOWN_HOOK;
    }

    public void addDisposable(Disposable disposable) {
        addDisposable(disposable, DEFAULT_PRIORITY);
    }

    public void addDisposable(Disposable disposable, int priority) {
        disposables.add(new DisposablePriorityWrapper(disposable, priority));
    }

    @Override
    public void run() {
        destroyAll();
    }

    public void destroyAll() {
        if (!destroyed.compareAndSet(false, true)) {
            return;
        }

        if (disposables.isEmpty()) {
            return;
        }

        log.info("destoryAll starting");

        while (!disposables.isEmpty()) {
            Disposable disposable = disposables.poll();
            disposable.destroy();
        }

        log.info("destoryAll finish");
    }

    /**
     * for spring context
     */
    public static void removeRuntimeShutdownHook() {
        Runtime.getRuntime().removeShutdownHook(SHUTDOWN_HOOK);
    }

    private static class DisposablePriorityWrapper implements Comparable<DisposablePriorityWrapper>, Disposable {

        private final Disposable disposable;

        private final int priority;

        public DisposablePriorityWrapper(Disposable disposable, int priority) {
            this.disposable = disposable;
            this.priority = priority;
        }

        @Override
        public int compareTo(DisposablePriorityWrapper challenger) {
            return priority - challenger.priority;
        }

        @Override
        public void destroy() {
            disposable.destroy();
        }
    }
}
