package com.liyz.auth.common.netty.serializer;

import com.liyz.auth.common.netty.compressor.Compressor;
import com.liyz.auth.common.netty.constant.CompressorType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:52
 */
public class SerializerFactory {

    /**
     * The constant COMPRESSOR_MAP.
     */
    protected static final Map<SerializerType, Serializer> SERIALIZER_MAP = new ConcurrentHashMap<>();

    static {
        SERIALIZER_MAP.put(SerializerType.FST, new FstSerializer());
        SERIALIZER_MAP.put(SerializerType.HESSIAN, new HessianSerializer());
        SERIALIZER_MAP.put(SerializerType.KRYO, new KryoSerializer());
        SERIALIZER_MAP.put(SerializerType.PROTOBUF, new ProtobufSerializer());
        SERIALIZER_MAP.put(SerializerType.LIYZ, new LiyzSerializer());
    }

    /**
     * Get compressor by code.
     *
     * @param code the code
     * @return the compressor
     */
    public static Serializer getSerializer(byte code) {
        SerializerType type = SerializerType.getByCode(code);
        return SERIALIZER_MAP.get(type);
    }
}
