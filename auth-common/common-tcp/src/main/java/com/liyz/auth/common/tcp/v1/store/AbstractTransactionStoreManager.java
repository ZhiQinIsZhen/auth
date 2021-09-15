package com.liyz.auth.common.tcp.v1.store;

import com.liyz.auth.common.tcp.v1.session.GlobalSession;
import com.liyz.auth.common.tcp.v1.session.SessionCondition;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/15 11:09
 */
public abstract class AbstractTransactionStoreManager implements TransactionStoreManager {

    @Override
    public GlobalSession readSession(String xid) {
        return null;
    }

    @Override
    public GlobalSession readSession(String xid, boolean withBranchSessions) {
        return null;
    }

    @Override
    public List<GlobalSession> readSession(SessionCondition sessionCondition) {
        return null;
    }

    @Override
    public void shutdown() {
    }
}
