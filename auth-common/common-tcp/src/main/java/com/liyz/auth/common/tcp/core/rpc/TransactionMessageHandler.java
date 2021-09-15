package com.liyz.auth.common.tcp.core.rpc;

import com.liyz.auth.common.tcp.core.protocol.AbstractMessage;
import com.liyz.auth.common.tcp.core.protocol.AbstractResultMessage;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 18:01
 */
public interface TransactionMessageHandler {

    /**
     * On a request received.
     *
     * @param request received request message
     * @param context context of the RPC
     * @return response to the request
     */
    AbstractResultMessage onRequest(AbstractMessage request, RpcContext context);

    /**
     * On a response received.
     *
     * @param response received response message
     * @param context  context of the RPC
     */
    void onResponse(AbstractResultMessage response, RpcContext context);
}
