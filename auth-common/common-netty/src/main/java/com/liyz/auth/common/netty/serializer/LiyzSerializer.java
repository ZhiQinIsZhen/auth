package com.liyz.auth.common.netty.serializer;

import com.liyz.auth.common.netty.loader.LoadLevel;
import com.liyz.auth.common.netty.protocol.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:43
 */
@LoadLevel(name = "LIYZ")
public class LiyzSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T t) {
        if (t == null || !(t instanceof AbstractMessage)) {
            throw new IllegalArgumentException("AbstractMessage isn't available.");
        }
        AbstractMessage abstractMessage = (AbstractMessage)t;
        //typecode
        short typecode = abstractMessage.getTypeCode();
        //msg codec
        MessageSeataCodec messageCodec = MessageCodecFactory.getMessageCodec(typecode);
        //get empty ByteBuffer
        ByteBuf out = Unpooled.buffer(1024);
        //msg encode
        messageCodec.encode(t, out);
        byte[] body = new byte[out.readableBytes()];
        out.readBytes(body);

        //typecode + body
        ByteBuffer byteBuffer = ByteBuffer.allocate(2 + body.length);
        byteBuffer.putShort(typecode);
        byteBuffer.put(body);

        byteBuffer.flip();
        byte[] content = new byte[byteBuffer.limit()];
        byteBuffer.get(content);
        return content;
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Nothing to decode.");
        }
        if (bytes.length < 2) {
            throw new IllegalArgumentException("The byte[] isn't available for decode.");
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        //typecode
        short typecode = byteBuffer.getShort();
        //msg body
        byte[] body = new byte[byteBuffer.remaining()];
        byteBuffer.get(body);
        ByteBuffer in = ByteBuffer.wrap(body);
        //new Messgae
        AbstractMessage abstractMessage = MessageCodecFactory.getMessage(typecode);
        //get messageCodec
        MessageSeataCodec messageCodec = MessageCodecFactory.getMessageCodec(typecode);
        //decode
        messageCodec.decode(abstractMessage, in);
        return (T)abstractMessage;
    }

}
