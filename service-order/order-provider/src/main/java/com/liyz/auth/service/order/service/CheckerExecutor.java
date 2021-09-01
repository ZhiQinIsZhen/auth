package com.liyz.auth.service.order.service;

import com.liyz.auth.service.order.context.ServiceResult;
import com.liyz.auth.service.order.context.StateContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * 注释:校验器的执行器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 14:16
 */
@Slf4j
@Service
public class CheckerExecutor {

    private ExecutorService executor = new ThreadPoolExecutor
            (
                    2,
                    10,
                    60,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
            );

    /**
     * 串行
     *
     * @param checkers
     * @param context
     * @param <T>
     * @param <C>
     * @return
     */
    public <T, C> ServiceResult<T> serialCheck(List<Checker> checkers, StateContext<C> context) {
        if (!CollectionUtils.isEmpty(checkers)) {
            for (Checker checker : checkers) {
                ServiceResult<T> result = checker.check(context);
                if (!result.isSuccess()) {
                    return result;
                }
            }
        }
        return new ServiceResult<>();
    }

    /**
     * 执行并行校验器，
     * 按照任务投递的顺序判断返回。
     */
    public <T, C> ServiceResult<T> parallelCheck(List<Checker> checkers, StateContext<C> context) {
        if (!CollectionUtils.isEmpty(checkers)) {
            if (checkers.size() == 1) {
                return checkers.get(0).check(context);
            }
            List<Future<ServiceResult>> resultList = Collections.synchronizedList(new ArrayList<>(checkers.size()));
            checkers.sort(Comparator.comparingInt(Checker::order));
            for (Checker c : checkers) {
                Future<ServiceResult> future = executor.submit(() -> c.check(context));
                resultList.add(future);
            }
            for (Future<ServiceResult> future : resultList) {
                try {
                    ServiceResult sr = future.get();
                    if (!sr.isSuccess()) {
                        return sr;
                    }
                } catch (Exception e) {
                    log.error("parallelCheck executor.submit error.", e);
                    throw new RuntimeException(e);
                }
            }
        }
        return new ServiceResult<>();
    }

    /**
     * 执行checker的释放操作
     */
    public <T, C> void releaseCheck(Checkable checkable, StateContext<C> context, ServiceResult<T> result) {
        List<Checker> checkers = new ArrayList<>();
        checkers.addAll(checkable.getParamChecker());
        checkers.addAll(checkable.getSyncChecker());
        checkers.addAll(checkable.getAsyncChecker());
        checkers.removeIf(Checker::needRelease);
        if (!CollectionUtils.isEmpty(checkers)) {
            if (checkers.size() == 1) {
                checkers.get(0).release(context, result);
                return;
            }
            CountDownLatch latch = new CountDownLatch(checkers.size());
            for (Checker c : checkers) {
                executor.execute(() -> {
                    try {
                        c.release(context, result);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
