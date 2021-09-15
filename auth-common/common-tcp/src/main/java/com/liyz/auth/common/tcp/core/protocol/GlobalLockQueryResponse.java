package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:22
 */
public class GlobalLockQueryResponse extends AbstractTransactionResponse{

    private boolean lockable = false;

    /**
     * Is lockable boolean.
     *
     * @return the boolean
     */
    public boolean isLockable() {
        return lockable;
    }

    /**
     * Sets lockable.
     *
     * @param lockable the lockable
     */
    public void setLockable(boolean lockable) {
        this.lockable = lockable;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_LOCK_QUERY_RESULT;
    }
}
