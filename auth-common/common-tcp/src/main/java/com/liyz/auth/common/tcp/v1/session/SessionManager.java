package com.liyz.auth.common.tcp.v1.session;

import com.liyz.auth.common.tcp.core.BranchSession;
import com.liyz.auth.common.tcp.core.BranchStatus;
import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;
import com.liyz.auth.common.tcp.v1.core.Disposable;

import java.util.Collection;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:26
 */
public interface SessionManager extends SessionLifecycleListener, Disposable {

    /**
     * Add global session.
     *
     * @param session the session
     */
    void addGlobalSession(GlobalSession session);

    /**
     * Find global session global session.
     *
     * @param xid the xid
     * @return the global session
     */
    GlobalSession findGlobalSession(String xid) ;

    /**
     * Find global session global session.
     *
     * @param xid the xid
     * @param withBranchSessions the withBranchSessions
     * @return the global session
     */
    GlobalSession findGlobalSession(String xid, boolean withBranchSessions);

    /**
     * Update global session status.
     *
     * @param session the session
     * @param status  the status
     */
    void updateGlobalSessionStatus(GlobalSession session, GlobalStatus status);

    /**
     * Remove global session.
     *
     * @param session the session
     */
    void removeGlobalSession(GlobalSession session);

    /**
     * Add branch session.
     *
     * @param globalSession the global session
     * @param session       the session
     */
    void addBranchSession(GlobalSession globalSession, BranchSession session);

    /**
     * Update branch session status.
     *
     * @param session the session
     * @param status  the status
     */
    void updateBranchSessionStatus(BranchSession session, BranchStatus status);

    /**
     * Remove branch session.
     *
     * @param globalSession the global session
     * @param session       the session
     */
    void removeBranchSession(GlobalSession globalSession, BranchSession session);

    /**
     * All sessions collection.
     *
     * @return the collection
     */
    Collection<GlobalSession> allSessions();

    /**
     * Find global sessions list.
     *
     * @param condition the condition
     * @return the list
     */
    List<GlobalSession> findGlobalSessions(SessionCondition condition);

    /**
     * lock and execute
     *
     * @param globalSession the global session
     * @param lockCallable the lock Callable
     * @return the value
     */
    <T> T lockAndExecute(GlobalSession globalSession, GlobalSession.LockCallable<T> lockCallable);
}
