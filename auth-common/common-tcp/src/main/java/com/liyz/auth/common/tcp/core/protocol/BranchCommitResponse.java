package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:55
 */
public class BranchCommitResponse extends AbstractBranchEndResponse{

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_COMMIT_RESULT;
    }
}
