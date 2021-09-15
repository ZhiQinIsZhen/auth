package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:10
 */
public abstract class AbstractGlobalEndResponse extends AbstractTransactionResponse{

    /**
     * The Global status.
     */
    protected GlobalStatus globalStatus;

    /**
     * Gets global status.
     *
     * @return the global status
     */
    public GlobalStatus getGlobalStatus() {
        return globalStatus;
    }

    /**
     * Sets global status.
     *
     * @param globalStatus the global status
     */
    public void setGlobalStatus(GlobalStatus globalStatus) {
        this.globalStatus = globalStatus;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("globalStatus=");
        result.append(globalStatus);
        result.append(",");
        result.append("ResultCode=");
        result.append(getResultCode());
        result.append(",");
        result.append("Msg=");
        result.append(getMsg());

        return result.toString();
    }
}
