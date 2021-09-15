package com.liyz.auth.common.tcp.core;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:37
 */
public interface Locker {

    /**
     * Acquire lock boolean.
     *
     * @param rowLock the row lock
     * @return the boolean
     */
    boolean acquireLock(List<RowLock> rowLock) ;

    /**
     * Un lock boolean.
     *
     * @param rowLock the row lock
     * @return the boolean
     */
    boolean releaseLock(List<RowLock> rowLock);

    /**
     * Un lock boolean.
     *
     * @param xid the xid
     * @param branchId the branchId
     * @return the boolean
     */
    boolean releaseLock(String xid, Long branchId);

    /**
     * Un lock boolean.
     *
     * @param xid the xid
     * @param branchIds the branchIds
     * @return the boolean
     */
    boolean releaseLock(String xid, List<Long> branchIds);

    /**
     * Is lockable boolean.
     *
     * @param rowLock the row lock
     * @return the boolean
     */
    boolean isLockable(List<RowLock> rowLock);

    /**
     * Clean all locks boolean.
     *
     * @return the boolean
     */
    void cleanAllLocks();
}
