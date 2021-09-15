package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:36
 */
public enum FlushDiskMode {

    /**
     * sync flush disk
     */
    SYNC_MODEL("sync"),
    /**
     * async flush disk
     */
    ASYNC_MODEL("async");

    private String modeStr;

    FlushDiskMode(String modeStr) {
        this.modeStr = modeStr;
    }

    public static FlushDiskMode findDiskMode(String modeStr) {
        if (SYNC_MODEL.modeStr.equals(modeStr)) {
            return SYNC_MODEL;
        }
        return ASYNC_MODEL;
    }
}
