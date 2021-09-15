package com.liyz.auth.common.tcp.core.protocol;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:36
 */
public class HeartbeatMessage implements MessageTypeAware, Serializable {
    private static final long serialVersionUID = -6040139770648377840L;

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
    public short getTypeCode() {
        return MessageType.TYPE_HEARTBEAT_MSG;
    }

    @Override
    public String toString() {
        return this.ping ? "services ping" : "services pong";
    }

    public boolean isPing() {
        return ping;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }
}
