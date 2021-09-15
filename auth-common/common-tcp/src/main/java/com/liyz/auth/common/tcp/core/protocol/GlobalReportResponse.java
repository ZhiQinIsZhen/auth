package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:24
 */
public class GlobalReportResponse extends AbstractGlobalEndResponse {

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_REPORT_RESULT;
    }
}
