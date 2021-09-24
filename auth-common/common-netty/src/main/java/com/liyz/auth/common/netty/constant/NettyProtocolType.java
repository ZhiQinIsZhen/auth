package com.liyz.auth.common.netty.constant;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 16:35
 */
public enum NettyProtocolType {

    /**
     * Tcp transport protocol type.
     */
    TCP("tcp"),

    /**
     * Unix domain socket transport protocol type.
     */
    UNIX_DOMAIN_SOCKET("unix-domain-socket");

    /**
     * The Name.
     */
    public final String name;

    NettyProtocolType(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @param name the name
     * @return the type
     */
    public static NettyProtocolType getType(String name) {
        name = name.trim().replace('-', '_');
        for (NettyProtocolType b : NettyProtocolType.values()) {
            if (b.name().equalsIgnoreCase(name)) {
                return b;
            }
        }
        throw new IllegalArgumentException("unknown type:" + name);
    }
}
