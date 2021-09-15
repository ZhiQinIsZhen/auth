package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:45
 */
public enum StoreMode {

    /**
     * file store
     */
    FILE("file"),

    /**
     * database store
     */
    DB("db"),

    /**
     * redis store
     */
    REDIS("redis");

    private String name;

    StoreMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * get value of store mode
     * @param name the mode name
     * @return the store mode
     */
    public static StoreMode get(String name) {
        for (StoreMode sm : StoreMode.class.getEnumConstants()) {
            if (sm.name.equalsIgnoreCase(name)) {
                return sm;
            }
        }
        throw new IllegalArgumentException("unknown store mode:" + name);
    }
}
