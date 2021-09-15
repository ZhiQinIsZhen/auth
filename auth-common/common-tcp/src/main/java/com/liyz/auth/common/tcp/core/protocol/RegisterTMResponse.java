package com.liyz.auth.common.tcp.core.protocol;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:52
 */
public class RegisterTMResponse extends AbstractIdentifyResponse implements Serializable {

    /**
     * Instantiates a new Register tm response.
     */
    public RegisterTMResponse() {
        this(true);
    }

    /**
     * Instantiates a new Register tm response.
     *
     * @param result the result
     */
    public RegisterTMResponse(boolean result) {
        super();
        setIdentified(result);
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_CLT_RESULT;
    }
}
