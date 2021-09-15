package com.liyz.auth.common.tcp.core.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:08
 */
public interface MessageTypeAware {

    /**
     * return the message type
     * @return
     */
    short getTypeCode();
}
