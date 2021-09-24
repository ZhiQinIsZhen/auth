package com.liyz.auth.common.netty.serializer;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:44
 */
public interface MessageSeataCodec {

    /**
     * Gets message type.
     *
     * @return the message type
     */
    Class<?> getMessageClassType();

    /**
     * Encode byte [ ].
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param out the out
     * @return the byte [ ]
     */
    <T> void encode(T t, ByteBuf out);

    /**
     * Decode.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param in  the in
     */
    <T> void decode(T t, ByteBuffer in);
}
