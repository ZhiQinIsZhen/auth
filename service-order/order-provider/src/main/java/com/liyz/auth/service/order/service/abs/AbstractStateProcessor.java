package com.liyz.auth.service.order.service.abs;

import com.liyz.auth.service.order.context.ServiceResult;
import com.liyz.auth.service.order.context.StateContext;
import com.liyz.auth.service.order.service.Checkable;
import com.liyz.auth.service.order.service.CheckerExecutor;
import com.liyz.auth.service.order.service.StateActionStep;
import com.liyz.auth.service.order.service.StateProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 注释:状态机处理器模板类
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:52
 */
@Slf4j
public abstract class AbstractStateProcessor<T, C> implements StateProcessor<T, C>, StateActionStep<T, C> {

    @Resource
    private CheckerExecutor checkerExecutor;

    @Override
    public ServiceResult<T> action(StateContext<C> context) {
        ServiceResult<T> result;
        Checkable checkable = this.getCheckable(context);
        // 参数校验器
        result = checkerExecutor.serialCheck(checkable.getParamChecker(), context);
        if (!result.isSuccess()) {
            return result;
        }
        // 数据准备
        this.prepare(context);
        // 串行校验器
        result = checkerExecutor.serialCheck(checkable.getSyncChecker(), context);
        if (!result.isSuccess()) {
            return result;
        }
        // 并行校验器
        result = checkerExecutor.parallelCheck(checkable.getAsyncChecker(), context);
        if (!result.isSuccess()) {
            return result;
        }
        // getNextState不能在prepare前，因为有的nextState是根据prepare中的数据转换而来
        String nextState = this.getNextState(context);
        //业务逻辑
        result = this.action(nextState, context);
        if (!result.isSuccess()) {
            return result;
        }
        //持久化
        result = this.save(nextState, context);
        if (!result.isSuccess()) {
            return result;
        }
        // after
        this.after(context);
        return result;
    }
}
