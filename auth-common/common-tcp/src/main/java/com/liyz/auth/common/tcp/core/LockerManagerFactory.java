package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.LockManager;
import com.liyz.auth.common.tcp.loader.EnhancedServiceLoader;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:31
 */
public class LockerManagerFactory {

    /**
     * the lock manager
     */
    private static final LockManager LOCK_MANAGER = EnhancedServiceLoader.load(LockManager.class,
            "db");

    /**
     * Get lock manager.
     *
     * @return the lock manager
     */
    public static LockManager getLockManager() {
        return LOCK_MANAGER;
    }
}
