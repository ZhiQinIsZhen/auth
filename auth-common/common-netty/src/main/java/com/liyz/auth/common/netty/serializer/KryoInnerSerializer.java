package com.liyz.auth.common.netty.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:04
 */
public class KryoInnerSerializer {

    private final Kryo kryo;

    public KryoInnerSerializer(Kryo kryo) {
        this.kryo = Objects.requireNonNull(kryo);
    }

    public Kryo getKryo() {
        return kryo;
    }

    public <T> byte[] serialize(T t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, t);
        output.close();
        return baos.toByteArray();
    }

    public <T> T deserialize(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Input input = new Input(bais);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }
}
