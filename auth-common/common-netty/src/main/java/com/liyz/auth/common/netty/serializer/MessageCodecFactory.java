package com.liyz.auth.common.netty.serializer;

import com.liyz.auth.common.netty.protocol.AbstractMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:44
 */
public class MessageCodecFactory {

    /**
     * The constant UTF8.
     */
    protected static final Charset UTF8 = StandardCharsets.UTF_8;

    /**
     * Get message codec message codec.
     *
     * @param abstractMessage the abstract message
     * @return the message codec
     */
    public static MessageSeataCodec getMessageCodec(AbstractMessage abstractMessage) {
        return getMessageCodec(abstractMessage.getTypeCode());
    }

    /**
     * Gets msg instance by code.
     *
     * @param typeCode the type code
     * @return the msg instance by code
     */
    public static MessageSeataCodec getMessageCodec(short typeCode) {
        MessageSeataCodec msgCodec = null;
        return msgCodec;
    }

    public static AbstractMessage getMessage(short typeCode) {
        AbstractMessage abstractMessage = null;
        return abstractMessage;
    }
}
