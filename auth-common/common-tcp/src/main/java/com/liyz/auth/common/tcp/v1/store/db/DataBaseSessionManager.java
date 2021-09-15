package com.liyz.auth.common.tcp.v1.store.db;

import com.liyz.auth.common.tcp.core.BranchSession;
import com.liyz.auth.common.tcp.core.BranchStatus;
import com.liyz.auth.common.tcp.core.SessionHolder;
import com.liyz.auth.common.tcp.v1.core.executor.Initialize;
import com.liyz.auth.common.tcp.v1.core.loader.LoadLevel;
import com.liyz.auth.common.tcp.v1.core.loader.Scope;
import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;
import com.liyz.auth.common.tcp.v1.session.AbstractSessionManager;
import com.liyz.auth.common.tcp.v1.session.GlobalSession;
import com.liyz.auth.common.tcp.v1.session.SessionCondition;
import com.liyz.auth.common.tcp.v1.store.TransactionStoreManager;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/15 11:03
 */
@Slf4j
@LoadLevel(name = "db", scope = Scope.PROTOTYPE)
public class DataBaseSessionManager extends AbstractSessionManager implements Initialize {

    /**
     * The Task name.
     */
    protected String taskName;

    /**
     * Instantiates a new Data base session manager.
     */
    public DataBaseSessionManager() {
        super();
    }

    /**
     * Instantiates a new Data base session manager.
     *
     * @param name the name
     */
    public DataBaseSessionManager(String name) {
        super();
        this.taskName = name;
    }

    @Override
    public void init() {
        transactionStoreManager = DataBaseTransactionStoreManager.getInstance();
    }

    @Override
    public void addGlobalSession(GlobalSession session) {
        if (StringUtils.isBlank(taskName)) {
            boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.GLOBAL_ADD, session);
            if (!ret) {
                throw new RemoteServiceException("addGlobalSession failed.");
            }
        } else {
            boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.GLOBAL_UPDATE, session);
            if (!ret) {
                throw new RemoteServiceException("addGlobalSession failed.");
            }
        }
    }

    @Override
    public void updateGlobalSessionStatus(GlobalSession session, GlobalStatus status) {
        if (StringUtils.isNotBlank(taskName)) {
            return;
        }
        session.setStatus(status);
        boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.GLOBAL_UPDATE, session);
        if (!ret) {
            throw new RemoteServiceException("updateGlobalSessionStatus failed.");
        }
    }

    /**
     * remove globalSession
     * 1. rootSessionManager remove normal globalSession
     * 2. retryCommitSessionManager and retryRollbackSessionManager remove retry expired globalSession
     * @param session the session
     */
    @Override
    public void removeGlobalSession(GlobalSession session) {
        boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.GLOBAL_REMOVE, session);
        if (!ret) {
            throw new RemoteServiceException("removeGlobalSession failed.");
        }
    }

    @Override
    public void addBranchSession(GlobalSession globalSession, BranchSession session) {
        if (StringUtils.isNotBlank(taskName)) {
            return;
        }
        boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.BRANCH_ADD, session);
        if (!ret) {
            throw new RemoteServiceException("addBranchSession failed.");
        }
    }

    @Override
    public void updateBranchSessionStatus(BranchSession session, BranchStatus status) {
        if (StringUtils.isNotBlank(taskName)) {
            return;
        }
        boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.BRANCH_UPDATE, session);
        if (!ret) {
            throw new RemoteServiceException("updateBranchSessionStatus failed.");
        }
    }

    @Override
    public void removeBranchSession(GlobalSession globalSession, BranchSession session) {
        if (StringUtils.isNotBlank(taskName)) {
            return;
        }
        boolean ret = transactionStoreManager.writeSession(TransactionStoreManager.LogOperation.BRANCH_REMOVE, session);
        if (!ret) {
            throw new RemoteServiceException("removeBranchSession failed.");
        }
    }

    @Override
    public GlobalSession findGlobalSession(String xid) {
        return this.findGlobalSession(xid, true);
    }

    @Override
    public GlobalSession findGlobalSession(String xid, boolean withBranchSessions) {
        return transactionStoreManager.readSession(xid, withBranchSessions);
    }

    @Override
    public Collection<GlobalSession> allSessions() {
        // get by taskName
        if (SessionHolder.ASYNC_COMMITTING_SESSION_MANAGER_NAME.equalsIgnoreCase(taskName)) {
            return findGlobalSessions(new SessionCondition(GlobalStatus.AsyncCommitting));
        } else if (SessionHolder.RETRY_COMMITTING_SESSION_MANAGER_NAME.equalsIgnoreCase(taskName)) {
            return findGlobalSessions(new SessionCondition(new GlobalStatus[] {GlobalStatus.CommitRetrying}));
        } else if (SessionHolder.RETRY_ROLLBACKING_SESSION_MANAGER_NAME.equalsIgnoreCase(taskName)) {
            return findGlobalSessions(new SessionCondition(new GlobalStatus[] {GlobalStatus.RollbackRetrying,
                    GlobalStatus.Rollbacking, GlobalStatus.TimeoutRollbacking, GlobalStatus.TimeoutRollbackRetrying}));
        } else {
            // all data
            return findGlobalSessions(new SessionCondition(new GlobalStatus[] {
                    GlobalStatus.UnKnown, GlobalStatus.Begin,
                    GlobalStatus.Committing, GlobalStatus.CommitRetrying, GlobalStatus.Rollbacking,
                    GlobalStatus.RollbackRetrying,
                    GlobalStatus.TimeoutRollbacking, GlobalStatus.TimeoutRollbackRetrying, GlobalStatus.AsyncCommitting}));
        }
    }

    @Override
    public List<GlobalSession> findGlobalSessions(SessionCondition condition) {
        // nothing need to do
        return transactionStoreManager.readSession(condition);
    }

    @Override
    public <T> T lockAndExecute(GlobalSession globalSession, GlobalSession.LockCallable<T> lockCallable) {
        return lockCallable.call();
    }
}
