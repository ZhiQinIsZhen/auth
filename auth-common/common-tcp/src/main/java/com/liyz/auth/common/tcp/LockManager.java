package com.liyz.auth.common.tcp;

import com.liyz.auth.common.tcp.core.BranchSession;
import com.liyz.auth.common.tcp.v1.session.GlobalSession;
import com.liyz.auth.common.tcp.exception.TransactionException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:31
 */
public interface LockManager {

    /**
     * Acquire lock boolean.
     *
     * @param branchSession the branch session
     * @return the boolean
     * @throws TransactionException the transaction exception
     */
    boolean acquireLock(BranchSession branchSession) throws TransactionException;

    /**
     * Un lock boolean.
     *
     * @param branchSession the branch session
     * @return the boolean
     * @throws TransactionException the transaction exception
     */
    boolean releaseLock(BranchSession branchSession) throws TransactionException;

    /**
     * Un lock boolean.
     *
     * @param globalSession the global session
     * @return the boolean
     * @throws TransactionException the transaction exception
     */
    boolean releaseGlobalSessionLock(GlobalSession globalSession) throws TransactionException;

    /**
     * Is lockable boolean.
     *
     * @param xid        the xid
     * @param resourceId the resource id
     * @param lockKey    the lock key
     * @return the boolean
     * @throws TransactionException the transaction exception
     */
    boolean isLockable(String xid, String resourceId, String lockKey) throws TransactionException;

    /**
     * Clean all locks.
     *
     * @throws TransactionException the transaction exception
     */
    void cleanAllLocks() throws TransactionException;
}
