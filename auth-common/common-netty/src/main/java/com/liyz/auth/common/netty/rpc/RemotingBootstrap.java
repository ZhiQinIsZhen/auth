package com.liyz.auth.common.netty.rpc;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 16:24
 */
public interface RemotingBootstrap {

    /**
     * Start.
     */
    void start();

    /**
     * Shutdown.
     */
    void shutdown();
}
