package com.liyz.auth.common.tcp.core.protocol;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:16
 */
public class BranchRegisterResponse extends AbstractTransactionResponse implements Serializable {

    private long branchId;

    /**
     * Gets branch id.
     *
     * @return the branch id
     */
    public long getBranchId() {
        return branchId;
    }

    /**
     * Sets branch id.
     *
     * @param branchId the branch id
     */
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_REGISTER_RESULT;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("BranchRegisterResponse: branchId=");
        result.append(branchId);
        result.append(",");
        result.append("result code =");
        result.append(getResultCode());
        result.append(",");
        result.append("getMsg =");
        result.append(getMsg());

        return result.toString();
    }
}
