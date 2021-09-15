package com.liyz.auth.common.tcp.v1.store;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:28
 */
public interface SessionStorable {

    /**
     * Encode byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] encode();

    /**
     * Decode.
     *
     * @param src the src
     */
    void decode(byte[] src);
}
