package com.liyz.auth.common.limit.service;

import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.common.limit.enums.LimitType;
import com.liyz.auth.common.limit.exception.LimitException;
import com.liyz.auth.common.limit.exception.LimitExceptionCodeEnum;
import com.liyz.auth.common.limit.strategy.LimitKeyService;
import com.liyz.auth.common.limit.strategy.LimitKeyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Pointcut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 16:20
 */
@Slf4j
public abstract class AbstractLimitService implements LimitService {

    private static ThreadLocal<Double> permitsPerSecond = new ThreadLocal<>();

    /**
     * 切点
     */
    @Pointcut("@annotation(com.liyz.auth.common.limit.annotation.Limits)")
    public void aspect() {}

    /**
     * 限流判断
     *
     * @param limits
     */
    protected void limit(Limits limits) {
        Limit[] limitArray = limits.value();
        if (limitArray != null && limitArray.length > 0) {
            Set<LimitType> set = new HashSet<>();
            String key = null;
            double count = 0.0;
            for (Limit limit : limitArray) {
                if (Objects.isNull(limit)
                        || set.contains(limit.type())
                        || (LimitType.TOTAL == limit.type() && limit.count() < 0)) {
                    continue;
                }
                LimitKeyService limitKeyService = LimitKeyStrategy.getService(limit.type());
                if (Objects.isNull(limitKeyService) || StringUtils.isBlank(key = limitKeyService.getKey(limit))) {
                    throw new LimitException(LimitExceptionCodeEnum.NoSupportLimitType);
                }
                count = limitKeyService.getTotalCount(limit);
                if (count == 0) {
                    throw new LimitException(LimitExceptionCodeEnum.LimitRequest);
                }
                if (count > 0) {
                    try {
                        permitsPerSecond.set(count);
                        if (!tryAcquire(key)) {
                            log.error("key:{} --> 触发了限流，每秒只能允许 {} 次访问", key, count);
                            throw new LimitException(LimitExceptionCodeEnum.OutLimitCount);
                        }
                    } finally {
                        permitsPerSecond.remove();
                    }
                }
                set.add(limit.type());
            }
        }
    }

    /**
     * 是否超出阈值
     *
     * @param key
     * @return
     */
    public boolean tryAcquire(final String key) {
        boolean value = true;
        try {
            value = getCacheLimit(key).tryAcquire();
        } catch (Exception e) {
            log.error("limit error", e);
        }
        return value;
    }

    public static Double getCount() {
        return permitsPerSecond.get();
    }

    public static void setCount(Double count) {
        permitsPerSecond.set(count);
    }

    public static void removeCount() {
        permitsPerSecond.remove();
    }
}
