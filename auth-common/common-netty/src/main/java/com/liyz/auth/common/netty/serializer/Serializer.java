package com.liyz.auth.common.netty.serializer;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:34
 */
public interface Serializer {

    /**
     * Encode object to byte[].
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the byte [ ]
     */
    <T> byte[] serialize(T t);

    /**
     * Decode t from byte[].
     *
     * @param <T>   the type parameter
     * @param bytes the bytes
     * @return the t
     */
    <T> T deserialize(byte[] bytes);
}
