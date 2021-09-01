package com.liyz.auth.service.order.service;

import java.util.Collections;
import java.util.List;

/**
 * 注释:状态机校验器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 14:15
 */
public interface Checkable {

    /**
     * 参数校验
     */
    default List<Checker> getParamChecker() {
        return Collections.EMPTY_LIST;
    }
    /**
     * 需同步执行的状态检查器
     */
    default List<Checker> getSyncChecker() {
        return Collections.EMPTY_LIST;
    }
    /**
     * 可异步执行的校验器
     */
    default List<Checker> getAsyncChecker() {
        return Collections.EMPTY_LIST;
    }
}
