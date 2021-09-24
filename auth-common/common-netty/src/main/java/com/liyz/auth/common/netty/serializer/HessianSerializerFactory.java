package com.liyz.auth.common.netty.serializer;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:59
 */
public class HessianSerializerFactory extends com.caucho.hessian.io.SerializerFactory {
    public static final com.caucho.hessian.io.SerializerFactory INSTANCE = new HessianSerializerFactory();

    private HessianSerializerFactory() {
        super();
    }

    public static SerializerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected Serializer loadSerializer(Class<?> cl) throws HessianProtocolException {
        return super.loadSerializer(cl);
    }

    @Override
    protected Deserializer loadDeserializer(Class cl) throws HessianProtocolException {
        return super.loadDeserializer(cl);
    }
}
