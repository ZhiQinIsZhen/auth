package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:35
 */
public class StoreConfig {

    /**
     * Default 16kb.
     */
    private static final int DEFAULT_MAX_BRANCH_SESSION_SIZE = 1024 * 16;

    /**
     * Default 512b.
     */
    private static final int DEFAULT_MAX_GLOBAL_SESSION_SIZE = 512;

    /**
     * Default 16kb.
     */
    private static final int DEFAULT_WRITE_BUFFER_SIZE = 1024 * 16;

    public static int getMaxBranchSessionSize() {
        return DEFAULT_MAX_BRANCH_SESSION_SIZE;
    }

    public static int getMaxGlobalSessionSize() {
        return DEFAULT_MAX_GLOBAL_SESSION_SIZE;
    }

    public static int getFileWriteBufferCacheSize() {
        return DEFAULT_WRITE_BUFFER_SIZE;
    }

    public static FlushDiskMode getFlushDiskMode() {
        return FlushDiskMode.ASYNC_MODEL;
    }
}
