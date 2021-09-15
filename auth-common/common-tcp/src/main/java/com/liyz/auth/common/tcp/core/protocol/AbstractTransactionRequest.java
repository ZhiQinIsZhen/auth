package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.core.rpc.RpcContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:59
 */
public abstract class AbstractTransactionRequest extends AbstractMessage{

    /**
     * Handle abstract transaction response.
     *
     * @param rpcContext the rpc context
     * @return the abstract transaction response
     */
    public abstract AbstractTransactionResponse handle(RpcContext rpcContext);
}
