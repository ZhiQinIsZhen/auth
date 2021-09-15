package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;
import com.liyz.auth.common.tcp.core.rpc.TransactionCoordinatorOutbound;
import com.liyz.auth.common.tcp.exception.TransactionException;
import com.liyz.auth.common.tcp.v1.session.GlobalSession;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 11:01
 */
public interface Core extends TransactionCoordinatorInbound, TransactionCoordinatorOutbound {

    /**
     * Do global commit.
     *
     * @param globalSession the global session
     * @param retrying      the retrying
     * @return is global commit.
     * @throws TransactionException the transaction exception
     */
    boolean doGlobalCommit(GlobalSession globalSession, boolean retrying) throws TransactionException;

    /**
     * Do global rollback.
     *
     * @param globalSession the global session
     * @param retrying      the retrying
     * @return is global rollback.
     * @throws TransactionException the transaction exception
     */
    boolean doGlobalRollback(GlobalSession globalSession, boolean retrying) throws TransactionException;

    /**
     * Do global report.
     *
     * @param globalSession the global session
     * @param xid           Transaction id.
     * @param param         the global status
     * @throws TransactionException the transaction exception
     */
    void doGlobalReport(GlobalSession globalSession, String xid, GlobalStatus param) throws TransactionException;
}
