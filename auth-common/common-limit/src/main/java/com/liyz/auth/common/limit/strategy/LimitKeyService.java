package com.liyz.auth.common.limit.strategy;

import com.liyz.auth.common.limit.annotation.Limit;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 16:52
 */
public interface LimitKeyService {

    String getKey(Limit limit);

    double getTotalCount(Limit limit);
}
