package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:53
 */
public abstract class AbstractTransactionRequestToRM extends AbstractTransactionRequest {

    /**
     * The Handler.
     */
    protected RMInboundHandler handler;

    /**
     * Sets rm inbound message handler.
     *
     * @param handler the handler
     */
    public void setRMInboundMessageHandler(RMInboundHandler handler) {
        this.handler = handler;
    }
}
