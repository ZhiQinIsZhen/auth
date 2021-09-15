package com.liyz.auth.common.tcp.v1.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 14:22
 */
@Getter
@Setter
public class ThreadFactoryProperties {

    private String bossThreadPrefix;

    private String workerThreadPrefix;

    private String serverExecutorThreadPrefix;

    private boolean shareBossWorker;

    private String clientSelectorThreadPrefix;

    private int clientSelectorThreadSize;

    private String clientWorkerThreadPrefix;

    private int bossThreadSize;

    private String workerThreadSize;
}
