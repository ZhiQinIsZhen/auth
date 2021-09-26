package com.liyz.auth.common.netty.rpc;

import com.liyz.auth.common.netty.processor.RemotingProcessor;
import com.liyz.auth.common.netty.protocol.AbstractMessage;
import com.liyz.auth.common.netty.protocol.MessageType;
import com.liyz.auth.common.netty.protocol.RpcMessage;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 16:24
 */
public interface RemotingClient {

    /**
     * client send sync request.
     *
     * @param channel client channel
     * @param msg     transaction message {@link io.seata.core.protocol}
     * @return server result message
     * @throws TimeoutException
     */
    Object sendSyncRequest(Channel channel, Object msg) throws TimeoutException;

    /**
     * client send async request.
     *
     * @param channel client channel
     * @param msg     transaction message {@link io.seata.core.protocol}
     */
    void sendAsyncRequest(Channel channel, Object msg);

    /**
     * client send async response.
     *
     * @param serverAddress server address
     * @param rpcMessage    rpc message from server request
     * @param msg           transaction message {@link io.seata.core.protocol}
     */
    void sendAsyncResponse(String serverAddress, RpcMessage rpcMessage, Object msg);

    /**
     * On register msg success.
     *
     * @param serverAddress  the server address
     * @param channel        the channel
     * @param response       the response
     * @param requestMessage the request message
     */
    void onRegisterMsgSuccess(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage);

    /**
     * On register msg fail.
     *
     * @param serverAddress  the server address
     * @param channel        the channel
     * @param response       the response
     * @param requestMessage the request message
     */
    void onRegisterMsgFail(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage);

    /**
     * register processor
     *
     * @param messageType {@link MessageType}
     * @param processor   {@link RemotingProcessor}
     * @param executor    thread pool
     */
    void registerProcessor(final int messageType, final RemotingProcessor processor, final ExecutorService executor);
}
