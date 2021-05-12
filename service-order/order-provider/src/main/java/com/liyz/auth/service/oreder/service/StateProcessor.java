package com.liyz.auth.service.oreder.service;

import com.liyz.auth.service.oreder.context.ServiceResult;
import com.liyz.auth.service.oreder.context.StateContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:38
 */
public interface StateProcessor<T, C> {

    ServiceResult<T> action(StateContext<C> context);
}
