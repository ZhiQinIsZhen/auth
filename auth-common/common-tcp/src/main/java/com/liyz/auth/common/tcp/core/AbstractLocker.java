package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:39
 */
@Slf4j
public abstract class AbstractLocker implements Locker{

    /**
     * The constant LOCK_SPLIT.
     */
    protected static final String LOCK_SPLIT = "^^^";

    /**
     * Convert to lock do list.
     *
     * @param locks the locks
     * @return the list
     */
    protected List<LockDO> convertToLockDO(List<RowLock> locks) {
        List<LockDO> lockDOs = new ArrayList<>();
        if (CollectionUtils.isEmpty(locks)) {
            return lockDOs;
        }
        for (RowLock rowLock : locks) {
            LockDO lockDO = new LockDO();
            lockDO.setBranchId(rowLock.getBranchId());
            lockDO.setPk(rowLock.getPk());
            lockDO.setResourceId(rowLock.getResourceId());
            lockDO.setRowKey(getRowKey(rowLock.getResourceId(), rowLock.getTableName(), rowLock.getPk()));
            lockDO.setXid(rowLock.getXid());
            lockDO.setTransactionId(rowLock.getTransactionId());
            lockDO.setTableName(rowLock.getTableName());
            lockDOs.add(lockDO);
        }
        return lockDOs;
    }

    /**
     * Get row key string.
     *
     * @param resourceId the resource id
     * @param tableName  the table name
     * @param pk         the pk
     * @return the string
     */
    protected String getRowKey(String resourceId, String tableName, String pk) {
        return new StringBuilder().append(resourceId).append(LOCK_SPLIT).append(tableName).append(LOCK_SPLIT).append(pk)
                .toString();
    }

    @Override
    public void cleanAllLocks() {

    }

    @Override
    public boolean releaseLock(String xid, Long branchId) {
        return false;
    }

    @Override
    public boolean releaseLock(String xid, List<Long> branchIds) {
        return false;
    }
}
