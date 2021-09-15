package com.liyz.auth.common.tcp.config;

import java.util.concurrent.ExecutorService;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:59
 */
public interface ConfigChangeListener {

    /**
     * Gets executor.
     *
     * @return the executor
     */
    ExecutorService getExecutor();

    /**
     * Receive config info.
     *
     * @param configInfo the config info
     */
    void receiveConfigInfo(final String configInfo);
}
