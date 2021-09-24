package com.liyz.auth.common.netty.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.liyz.auth.common.netty.loader.LoadLevel;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:57
 */
@Slf4j
@LoadLevel(name = "HESSIAN")
public class HessianSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T t) {
        byte[] stream = null;
        com.caucho.hessian.io.SerializerFactory hessian = HessianSerializerFactory.getInstance();
        try {
            com.caucho.hessian.io.Serializer serializer = hessian.getSerializer(t.getClass());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Hessian2Output output = new Hessian2Output(baos);
            serializer.writeObject(t, output);
            output.close();
            stream = baos.toByteArray();
        } catch (IOException e) {
            log.error("Hessian encode error:{}", e.getMessage(), e);
        }
        return stream;
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        T obj = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);) {
            Hessian2Input input = new Hessian2Input(is);
            obj = (T) input.readObject();
            input.close();
        } catch (IOException e) {
            log.error("Hessian decode error:{}", e.getMessage(), e);
        }
        return obj;
    }
}
