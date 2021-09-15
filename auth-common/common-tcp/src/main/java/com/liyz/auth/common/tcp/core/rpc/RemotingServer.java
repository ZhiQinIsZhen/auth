package com.liyz.auth.common.tcp.core.rpc;

import com.liyz.auth.common.tcp.core.protocol.MessageType;
import com.liyz.auth.common.tcp.core.protocol.RpcMessage;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:26
 */
public interface RemotingServer {

    /**
     * server send sync request.
     *
     * @param resourceId rm client resourceId
     * @param clientId   rm client id
     * @param msg        transaction message {@link io.seata.core.protocol}
     * @return client result message
     * @throws TimeoutException
     */
    Object sendSyncRequest(String resourceId, String clientId, Object msg) throws TimeoutException;

    /**
     * server send sync request.
     *
     * @param channel client channel
     * @param msg     transaction message {@link io.seata.core.protocol}
     * @return client result message
     * @throws TimeoutException
     */
    Object sendSyncRequest(Channel channel, Object msg) throws TimeoutException;

    /**
     * server send async request.
     *
     * @param channel client channel
     * @param msg     transaction message {@link io.seata.core.protocol}
     */
    void sendAsyncRequest(Channel channel, Object msg);

    /**
     * server send async response.
     *
     * @param rpcMessage rpc message from client request
     * @param channel    client channel
     * @param msg        transaction message {@link io.seata.core.protocol}
     */
    void sendAsyncResponse(RpcMessage rpcMessage, Channel channel, Object msg);

    /**
     * register processor
     *
     * @param messageType {@link MessageType}
     * @param processor   {@link RemotingProcessor}
     * @param executor    thread pool
     */
    void registerProcessor(final int messageType, final RemotingProcessor processor, final ExecutorService executor);
}
