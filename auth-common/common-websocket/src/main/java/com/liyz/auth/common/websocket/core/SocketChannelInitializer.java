package com.liyz.auth.common.websocket.core;

import com.liyz.auth.common.websocket.protocol.v1.ProtocolV1Decoder;
import com.liyz.auth.common.websocket.protocol.v1.ProtocolV1Encoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 15:04
 */
@Slf4j
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private String socketPath;
    private int readerIdleTime;
    private int writerIdleTime;
    private int allIdleTime;
    private ChannelHandler[] channelHandlers;

    public SocketChannelInitializer(String socketPath, int readerIdleTime, int writerIdleTime, int allIdleTime,
                                    ChannelHandler[] channelHandlers) {
        this.socketPath = socketPath;
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
        this.allIdleTime = allIdleTime;
        this.channelHandlers = channelHandlers;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        log.info("initChannel .... {}", socketChannel);
        final ChannelPipeline pipeline = socketChannel.pipeline();
        // HttpServerCodec ,将请求和应答消息编码或者解码为HTTP消息
        pipeline.addLast("codec-http", new HttpServerCodec());
        // 将HTTP消息的多个部分组合成一条完整的HTTP消息;
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        // WebSocket升级
        pipeline.addLast("web-socket", new WebSocketServerProtocolHandler(socketPath, null, true));
        // 空闲检查, 60秒就关闭连接
        pipeline.addLast("idle", new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.SECONDS));
        // 心跳处理
        //pipeline.addLast("heart", new HeartbeatHandler());
        // 编码转换器
        pipeline.addLast("decoder", new ProtocolV1Decoder());
        pipeline.addLast("encoder", new ProtocolV1Encoder());
        if (channelHandlers != null && channelHandlers.length > 0 ) {
            pipeline.addLast(channelHandlers);
        }
    }
}
