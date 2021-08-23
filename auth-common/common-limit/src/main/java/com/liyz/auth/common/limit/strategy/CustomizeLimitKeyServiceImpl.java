package com.liyz.auth.common.limit.strategy;

import com.liyz.auth.common.limit.annotation.Limit;
import lombok.extern.slf4j.Slf4j;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/29 14:07
 */
@Slf4j
public class CustomizeLimitKeyServiceImpl implements LimitKeyService{

    @Override
    public String getKey(Limit limit) {
        return limit.key();
    }

    @Override
    public double getTotalCount(Limit limit) {
        return 1;
    }
}
