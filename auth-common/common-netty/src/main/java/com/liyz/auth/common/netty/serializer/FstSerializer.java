package com.liyz.auth.common.netty.serializer;

import com.liyz.auth.common.netty.loader.LoadLevel;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:53
 */
@LoadLevel(name = "FST")
public class FstSerializer implements Serializer {

    private FstSerializerFactory fstFactory = FstSerializerFactory.getDefaultFactory();

    @Override
    public <T> byte[] serialize(T t) {
        return fstFactory.serialize(t);
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        return (T)fstFactory.deserialize(bytes);
    }

}
