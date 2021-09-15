package com.liyz.auth.common.tcp.core.rpc.processor;

import com.liyz.auth.common.tcp.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:41
 */
@Slf4j
public class BatchLogHandler {

    private static final BlockingQueue<String> LOG_QUEUE = new LinkedBlockingQueue<>();

    public static final BatchLogHandler INSTANCE = new BatchLogHandler();

    private static final int MAX_LOG_SEND_THREAD = 1;
    private static final int MAX_LOG_TAKE_SIZE = 1024;
    private static final long KEEP_ALIVE_TIME = 0L;
    private static final String THREAD_PREFIX = "batchLoggerPrint";
    private static final long BUSY_SLEEP_MILLS = 5L;

    static {
        ExecutorService mergeSendExecutorService = new ThreadPoolExecutor(MAX_LOG_SEND_THREAD, MAX_LOG_SEND_THREAD,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new NamedThreadFactory(THREAD_PREFIX, MAX_LOG_SEND_THREAD, true));
        mergeSendExecutorService.submit(new BatchLogRunnable());
    }

    public BlockingQueue<String> getLogQueue() {
        return LOG_QUEUE;
    }

    /**
     * The type Batch log runnable.
     */
    static class BatchLogRunnable implements Runnable {

        @Override
        public void run() {
            List<String> logList = new ArrayList<>();
            while (true) {
                try {
                    logList.add(LOG_QUEUE.take());
                    LOG_QUEUE.drainTo(logList, MAX_LOG_TAKE_SIZE);
                    if (log.isInfoEnabled()) {
                        for (String str : logList) {
                            log.info(str);
                        }
                    }
                    logList.clear();
                    TimeUnit.MILLISECONDS.sleep(BUSY_SLEEP_MILLS);
                } catch (InterruptedException exx) {
                    log.error("batch log busy sleep error:{}", exx.getMessage(), exx);
                }

            }
        }
    }
}
