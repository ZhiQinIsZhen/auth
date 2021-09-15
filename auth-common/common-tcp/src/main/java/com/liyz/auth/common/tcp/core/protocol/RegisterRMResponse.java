package com.liyz.auth.common.tcp.core.protocol;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:49
 */
public class RegisterRMResponse extends AbstractIdentifyResponse implements Serializable {

    /**
     * Instantiates a new Register rm response.
     */
    public RegisterRMResponse() {
        this(true);
    }

    /**
     * Instantiates a new Register rm response.
     *
     * @param result the result
     */
    public RegisterRMResponse(boolean result) {
        super();
        setIdentified(result);
    }

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_REG_RM_RESULT;
    }
}
