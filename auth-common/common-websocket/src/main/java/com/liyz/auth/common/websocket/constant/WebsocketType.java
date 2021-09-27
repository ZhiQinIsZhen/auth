package com.liyz.auth.common.websocket.constant;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 10:50
 */
public enum WebsocketType {

    /**
     * Native transport server type.
     */
    NATIVE("native"),
    /**
     * Nio transport server type.
     */
    NIO("nio");

    /**
     * The Name.
     */
    public final String name;

    WebsocketType(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @param name the name
     * @return the type
     */
    public static WebsocketType getType(String name) {
        for (WebsocketType b : WebsocketType.values()) {
            if (b.name().equalsIgnoreCase(name)) {
                return b;
            }
        }
        throw new IllegalArgumentException("unknown type:" + name);
    }
}
