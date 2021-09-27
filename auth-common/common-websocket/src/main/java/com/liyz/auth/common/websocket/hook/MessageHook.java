package com.liyz.auth.common.websocket.hook;

import com.liyz.auth.common.websocket.message.BaseMessage;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 14:16
 */
public interface MessageHook {

    void doBeforeRequest(String channelId, BaseMessage request);

    void doAfterResponse(String channelId, BaseMessage request, Object response);
}
