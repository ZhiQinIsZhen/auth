package com.liyz.auth.common.netty.serializer;

import org.nustaq.serialization.FSTConfiguration;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:54
 */
public class FstSerializerFactory {

    private static final FstSerializerFactory FACTORY = new FstSerializerFactory();

    private final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();


    public static FstSerializerFactory getDefaultFactory() {
        return FACTORY;
    }

    public FstSerializerFactory() {
        SerializerClassRegistry.getRegisteredClasses().keySet().forEach(conf::registerClass);
    }

    public <T> byte[] serialize(T t) {
        return conf.asByteArray(t);
    }

    public <T> T deserialize(byte[] bytes) {
        return (T)conf.asObject(bytes);
    }
}
