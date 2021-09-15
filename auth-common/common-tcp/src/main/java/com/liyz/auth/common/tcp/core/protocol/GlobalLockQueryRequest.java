package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.core.rpc.RpcContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:21
 */
public class GlobalLockQueryRequest extends BranchRegisterRequest{

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_LOCK_QUERY;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }
}
