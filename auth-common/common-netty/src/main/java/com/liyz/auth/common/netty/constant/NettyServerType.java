package com.liyz.auth.common.netty.constant;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 16:32
 */
public enum NettyServerType {

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

    NettyServerType(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @param name the name
     * @return the type
     */
    public static NettyServerType getType(String name) {
        for (NettyServerType b : NettyServerType.values()) {
            if (b.name().equalsIgnoreCase(name)) {
                return b;
            }
        }
        throw new IllegalArgumentException("unknown type:" + name);
    }
}
