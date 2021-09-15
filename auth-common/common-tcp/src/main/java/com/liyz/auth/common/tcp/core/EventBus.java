package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 11:07
 */
public interface EventBus {

    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Event event);
}
