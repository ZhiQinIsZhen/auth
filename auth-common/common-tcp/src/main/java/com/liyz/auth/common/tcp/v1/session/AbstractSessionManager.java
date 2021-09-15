package com.liyz.auth.common.tcp.v1.session;

import com.liyz.auth.common.tcp.core.BranchSession;
import com.liyz.auth.common.tcp.core.BranchStatus;
import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;
import com.liyz.auth.common.tcp.v1.store.SessionStorable;
import com.liyz.auth.common.tcp.v1.store.TransactionStoreManager;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/15 10:47
 */
@Slf4j
public abstract class AbstractSessionManager implements SessionManager, SessionLifecycleListener{

    /**
     * The Transaction store manager.
     */
    protected TransactionStoreManager transactionStoreManager;

    /**
     * The Name.
     */
    protected String name;

    /**
     * Instantiates a new Abstract session manager.
     */
    public AbstractSessionManager() {
    }

    /**
     * Instantiates a new Abstract session manager.
     *
     * @param name the name
     */
    public AbstractSessionManager(String name) {
        this.name = name;
    }

    @Override
    public void addGlobalSession(GlobalSession session) {
        log.info("MANAGER[{}] SESSION[{}}] {}", name, session, TransactionStoreManager.LogOperation.GLOBAL_ADD);
        writeSession(TransactionStoreManager.LogOperation.GLOBAL_ADD, session);
    }

    @Override
    public void updateGlobalSessionStatus(GlobalSession session, GlobalStatus status) {
        log.info("MANAGER[{}] SESSION[{}}] {}", name, session, TransactionStoreManager.LogOperation.GLOBAL_UPDATE);
        writeSession(TransactionStoreManager.LogOperation.GLOBAL_UPDATE, session);
    }

    @Override
    public void removeGlobalSession(GlobalSession session) {
        log.info("MANAGER[{}] SESSION[{}}] {}", name, session, TransactionStoreManager.LogOperation.GLOBAL_REMOVE);
        writeSession(TransactionStoreManager.LogOperation.GLOBAL_REMOVE, session);
    }

    @Override
    public void addBranchSession(GlobalSession session, BranchSession branchSession) {
        log.info("MANAGER[{}] SESSION[{}}] {}", name, branchSession, TransactionStoreManager.LogOperation.BRANCH_ADD);
        writeSession(TransactionStoreManager.LogOperation.BRANCH_ADD, branchSession);
    }

    @Override
    public void updateBranchSessionStatus(BranchSession branchSession, BranchStatus status) {
        log.info("MANAGER[{}] SESSION[{}}] {}", name, branchSession, TransactionStoreManager.LogOperation.BRANCH_UPDATE);
        writeSession(TransactionStoreManager.LogOperation.BRANCH_UPDATE, branchSession);
    }

    @Override
    public void removeBranchSession(GlobalSession globalSession, BranchSession branchSession) {
        log.info("MANAGER[{}] SESSION[{}}] {}", name, branchSession, TransactionStoreManager.LogOperation.BRANCH_REMOVE);
        writeSession(TransactionStoreManager.LogOperation.BRANCH_REMOVE, branchSession);
    }

    @Override
    public void onBegin(GlobalSession globalSession) {
        addGlobalSession(globalSession);
    }

    @Override
    public void onStatusChange(GlobalSession globalSession, GlobalStatus status) {
        updateGlobalSessionStatus(globalSession, status);
    }

    @Override
    public void onBranchStatusChange(GlobalSession globalSession, BranchSession branchSession, BranchStatus status) {
        updateBranchSessionStatus(branchSession, status);
    }

    @Override
    public void onAddBranch(GlobalSession globalSession, BranchSession branchSession) {
        addBranchSession(globalSession, branchSession);
    }

    @Override
    public void onRemoveBranch(GlobalSession globalSession, BranchSession branchSession) {
        removeBranchSession(globalSession, branchSession);
    }

    @Override
    public void onClose(GlobalSession globalSession) {
        globalSession.setActive(false);
    }

    @Override
    public void onEnd(GlobalSession globalSession) {
        removeGlobalSession(globalSession);
    }

    private void writeSession(TransactionStoreManager.LogOperation logOperation, SessionStorable sessionStorable) {
        if (!transactionStoreManager.writeSession(logOperation, sessionStorable)) {
            if (TransactionStoreManager.LogOperation.GLOBAL_ADD.equals(logOperation)) {
                throw new RemoteServiceException("Fail to store global session");
            } else if (TransactionStoreManager.LogOperation.GLOBAL_UPDATE.equals(logOperation)) {
                throw new RemoteServiceException("Fail to update global session");
            } else if (TransactionStoreManager.LogOperation.GLOBAL_REMOVE.equals(logOperation)) {
                throw new RemoteServiceException("Fail to remove global session");
            } else if (TransactionStoreManager.LogOperation.BRANCH_ADD.equals(logOperation)) {
                throw new RemoteServiceException("Fail to store branch session");
            } else if (TransactionStoreManager.LogOperation.BRANCH_UPDATE.equals(logOperation)) {
                throw new RemoteServiceException("Fail to update branch session");
            } else if (TransactionStoreManager.LogOperation.BRANCH_REMOVE.equals(logOperation)) {
                throw new RemoteServiceException("Fail to remove branch session");
            } else {
                throw new RemoteServiceException("Unknown LogOperation:" + logOperation.name());
            }
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Sets transaction store manager.
     *
     * @param transactionStoreManager the transaction store manager
     */
    public void setTransactionStoreManager(TransactionStoreManager transactionStoreManager) {
        this.transactionStoreManager = transactionStoreManager;
    }
}
