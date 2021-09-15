package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:20
 */
public class BranchReportResponse extends AbstractTransactionResponse{

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_STATUS_REPORT_RESULT;
    }
}
