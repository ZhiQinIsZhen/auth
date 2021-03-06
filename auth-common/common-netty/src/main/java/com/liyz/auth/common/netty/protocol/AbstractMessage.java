package com.liyz.auth.common.netty.protocol;

import com.liyz.auth.common.netty.constant.NettyServerConstant;
import com.liyz.auth.common.util.JsonMapperUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:02
 */
@Slf4j
public abstract class AbstractMessage implements MessageTypeAware, Serializable {
    private static final long serialVersionUID = -8412383625407835621L;

    /**
     * The constant UTF8.
     */
    protected static final Charset UTF8 = NettyServerConstant.DEFAULT_CHARSET;
    /**
     * The Ctx.
     */
    protected ChannelHandlerContext ctx;

    /**
     * Bytes to int int.
     *
     * @param bytes  the bytes
     * @param offset the offset
     * @return the int
     */
    public static int bytesToInt(byte[] bytes, int offset) {
        int ret = 0;
        for (int i = 0; i < 4 && i + offset < bytes.length; i++) {
            ret <<= 8;
            ret |= (int)bytes[i + offset] & 0xFF;
        }
        return ret;
    }

    /**
     * Int to bytes.
     *
     * @param i      the
     * @param bytes  the bytes
     * @param offset the offset
     */
    public static void intToBytes(int i, byte[] bytes, int offset) {
        bytes[offset] = (byte)((i >> 24) & 0xFF);
        bytes[offset + 1] = (byte)((i >> 16) & 0xFF);
        bytes[offset + 2] = (byte)((i >> 8) & 0xFF);
        bytes[offset + 3] = (byte)(i & 0xFF);
    }

    @Override
    public String toString() {
        return JsonMapperUtil.toJSONString(this);
    }
}
