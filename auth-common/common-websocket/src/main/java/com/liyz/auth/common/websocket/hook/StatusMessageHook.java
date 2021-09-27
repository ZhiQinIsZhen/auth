package com.liyz.auth.common.websocket.hook;

import com.liyz.auth.common.websocket.core.MessageStatus;
import com.liyz.auth.common.websocket.message.BaseMessage;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 14:18
 */
public class StatusMessageHook implements MessageHook{
    @Override
    public void doBeforeRequest(String channelId, BaseMessage request) {
        MessageStatus.beginCount(channelId);
    }

    @Override
    public void doAfterResponse(String channelId, BaseMessage request, Object response) {
        MessageStatus.endCount(channelId);
    }
}
