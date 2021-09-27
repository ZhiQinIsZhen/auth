package com.liyz.auth.common.websocket.protocol.v1;

import com.liyz.auth.common.websocket.message.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * 注释:消息体解码
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 15:10
 */
@ChannelHandler.Sharable
public class ProtocolV1Decoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final int length = byteBuf.readableBytes();
        final byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);

        MessagePack pack = new MessagePack();
        BaseMessage req = pack.read(array, BaseMessage.class);
        list.add(req);
    }
}
