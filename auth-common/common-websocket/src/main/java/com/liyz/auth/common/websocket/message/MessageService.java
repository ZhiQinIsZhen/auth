package com.liyz.auth.common.websocket.message;

import com.liyz.auth.common.websocket.processor.MessageProcessor;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 13:59
 */
public interface MessageService {

    /**
     * server send sync request.
     *
     * @param channel
     * @param msg
     * @return
     */
    Object sendSyncRequest(Channel channel, Object msg) throws TimeoutException;

    /**
     * server send async request.
     *
     * @param channel
     * @param msg
     */
    void sendAsyncRequest(Channel channel, Object msg);

    /**
     * server send async response.
     *
     * @param baseMessage
     * @param channel
     * @param msg
     */
    void sendAsyncResponse(BaseMessage baseMessage, Channel channel, Object msg);

    /**
     * registerProcessor
     *
     * @param messageType
     * @param processor
     * @param executor
     */
    void registerProcessor(final String messageType, final MessageProcessor processor, final ExecutorService executor);
}
