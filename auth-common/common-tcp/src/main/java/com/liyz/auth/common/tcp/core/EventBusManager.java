package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 11:05
 */
public class EventBusManager {

    private static class SingletonHolder {
        private static EventBus INSTANCE = new GuavaEventBus("tc");
    }

    public static EventBus get() {
        return SingletonHolder.INSTANCE;
    }
}
