package com.liyz.auth.common.tcp.core.rpc;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:28
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
