package com.liyz.auth.common.websocket.protocol.v1;

import com.liyz.auth.common.websocket.message.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 注释:消息返回体编码
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 15:14
 */
@ChannelHandler.Sharable
public class ProtocolV1Encoder extends MessageToByteEncoder<BaseMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseMessage baseMessage, ByteBuf byteBuf) throws Exception {
        // 创建MessagePack对象
        MessagePack pack = new MessagePack();
        // 将对象编码为MessagePack格式的字节数组
        byteBuf.writeBytes(pack.write(baseMessage));
    }
}
