package com.liyz.auth.common.netty.protocol;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:26
 */
public interface MessageTypeAware {

    /**
     * return the message type
     * @return
     */
    short getTypeCode();
}
