package com.liyz.auth.common.netty.rpc;

import com.liyz.auth.common.netty.processor.RemotingProcessor;
import com.liyz.auth.common.netty.protocol.RpcMessage;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/22 14:12
 */
public interface RemotingServer {

    /**
     * server send sync request.
     *
     * @param resourceId
     * @param clientId
     * @param msg
     * @return
     */
    Object sendSyncRequest(String resourceId, String clientId, Object msg) throws TimeoutException;

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
     * @param rpcMessage
     * @param channel
     * @param msg
     */
    void sendAsyncResponse(RpcMessage rpcMessage, Channel channel, Object msg);

    /**
     * register processor
     *
     * @param messageType {@link com.liyz.auth.common.netty.protocol.MessageType}
     * @param processor
     * @param executor
     */
    void registerProcessor(final int messageType, final RemotingProcessor processor, final ExecutorService executor);
}
