package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.core.rpc.TCInboundHandler;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:01
 */
public abstract class AbstractTransactionRequestToTC extends AbstractTransactionRequest {

    /**
     * The Handler.
     */
    protected TCInboundHandler handler;

    /**
     * Sets tc inbound handler.
     *
     * @param handler the handler
     */
    public void setTCInboundHandler(TCInboundHandler handler) {
        this.handler = handler;
    }
}
