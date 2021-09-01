package com.liyz.auth.service.order.service.impl;

import com.liyz.auth.service.order.context.ServiceResult;
import com.liyz.auth.service.order.context.StateContext;
import com.liyz.auth.service.order.service.Checker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 15:38
 */
@Slf4j
@Service
public class UserChecker<T, C> implements Checker<T, C> {

    @Override
    public ServiceResult<T> check(StateContext<C> context) {
        log.info("用户校验。。。。。。");
        return new ServiceResult<>();
    }
}
