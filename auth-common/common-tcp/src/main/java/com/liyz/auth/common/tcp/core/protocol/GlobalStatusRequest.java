package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.core.rpc.RpcContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:22
 */
public class GlobalStatusRequest extends AbstractGlobalEndRequest{

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_GLOBAL_STATUS;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }
}
