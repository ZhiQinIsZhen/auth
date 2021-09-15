package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.core.rpc.RpcContext;
import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:24
 */
public class GlobalReportRequest extends AbstractGlobalEndRequest {

    /**
     * The Global status.
     */
    protected GlobalStatus globalStatus;

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_REPORT;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

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
}
