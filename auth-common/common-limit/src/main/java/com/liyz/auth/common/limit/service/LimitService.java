package com.liyz.auth.common.limit.service;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 16:12
 */
public interface LimitService {

    /**
     * 获取限流信息
     *
     * @param key
     * @return
     */
    RateLimiter getCacheLimit(final String key);
}
