package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:57
 */
public class BranchRollbackResponse extends AbstractBranchEndResponse{

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_ROLLBACK_RESULT;
    }
}
