package com.liyz.auth.common.tcp.core.rpc;

import com.liyz.auth.common.tcp.core.BranchSession;
import com.liyz.auth.common.tcp.core.BranchStatus;
import com.liyz.auth.common.tcp.v1.session.GlobalSession;
import com.liyz.auth.common.tcp.exception.TransactionException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 11:01
 */
public interface TransactionCoordinatorOutbound {

    /**
     * Commit a branch transaction.
     *
     * @param globalSession the global session
     * @param branchSession the branch session
     * @return Status of the branch after committing.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown
     *                              out.
     */
    BranchStatus branchCommit(GlobalSession globalSession, BranchSession branchSession) throws TransactionException;

    /**
     * Rollback a branch transaction.
     *
     * @param globalSession the global session
     * @param branchSession the branch session
     * @return Status of the branch after rollbacking.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown
     *                              out.
     */
    BranchStatus branchRollback(GlobalSession globalSession, BranchSession branchSession) throws TransactionException;
}
