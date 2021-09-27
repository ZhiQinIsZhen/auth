package com.liyz.auth.common.websocket.message.impl;

import com.liyz.auth.common.websocket.message.MessageTypeAware;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 16:56
 */
public class HeartbeatMessage implements MessageTypeAware, Serializable {
    private static final long serialVersionUID = 2443770366290926515L;

    @Getter
    @Setter
    private boolean ping = true;

    /**
     * The constant PING.
     */
    public static final HeartbeatMessage PING = new HeartbeatMessage(true);
    /**
     * The constant PONG.
     */
    public static final HeartbeatMessage PONG = new HeartbeatMessage(false);

    private HeartbeatMessage(boolean ping) {
        this.ping = ping;
    }

    @Override
    public String getTypeCode() {
        return "heartbeat";
    }

    @Override
    public String toString() {
        return this.ping ? "services ping" : "services pong";
    }
}
