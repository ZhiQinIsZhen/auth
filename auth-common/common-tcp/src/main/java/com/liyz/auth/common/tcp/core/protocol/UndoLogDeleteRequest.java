package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.core.BranchType;
import com.liyz.auth.common.tcp.core.rpc.RpcContext;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:53
 */
public class UndoLogDeleteRequest extends AbstractTransactionRequestToRM implements Serializable {

    private static final long serialVersionUID = 7539732523682335742L;

    public static final short DEFAULT_SAVE_DAYS = 7;

    private String resourceId;

    private short saveDays = DEFAULT_SAVE_DAYS;

    /**
     * The Branch type.
     */
    protected BranchType branchType = BranchType.AT;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public short getSaveDays() {
        return saveDays;
    }

    public void setSaveDays(short saveDays) {
        this.saveDays = saveDays;
    }

    public BranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        handler.handle(this);
        return null;
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_RM_DELETE_UNDOLOG;
    }

    @Override
    public String toString() {
        return "UndoLogDeleteRequest{" +
                "resourceId='" + resourceId + '\'' +
                ", saveDays=" + saveDays +
                ", branchType=" + branchType +
                '}';
    }
}
