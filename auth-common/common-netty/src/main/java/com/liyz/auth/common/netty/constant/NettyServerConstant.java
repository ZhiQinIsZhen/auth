package com.liyz.auth.common.netty.constant;

import java.nio.charset.Charset;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 17:04
 */
public interface NettyServerConstant {

    /**
     * The constant IP_PORT_SPLIT_CHAR.
     */
    String IP_PORT_SPLIT_CHAR = ":";
    /**
     * The constant CLIENT_ID_SPLIT_CHAR.
     */
    String CLIENT_ID_SPLIT_CHAR = ":";
    /**
     * The constant ENDPOINT_BEGIN_CHAR.
     */
    String ENDPOINT_BEGIN_CHAR = "/";
    /**
     * The constant DBKEYS_SPLIT_CHAR.
     */
    String DBKEYS_SPLIT_CHAR = ",";
    /**
     * Shutdown timeout default 3s
     */
    int DEFAULT_SHUTDOWN_TIMEOUT_SEC = 3;
    int DEFAULT_SELECTOR_THREAD_SIZE = 1;
    int DEFAULT_BOSS_THREAD_SIZE = 1;


    String DEFAULT_BOSS_THREAD_PREFIX = "NettyBoss";
    String DEFAULT_NIO_WORKER_THREAD_PREFIX = "NettyServerNIOWorker";
    String DEFAULT_EXECUTOR_THREAD_PREFIX = "NettyServerBizHandler";

    /**
     * default charset name
     */
    String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * default charset is utf-8
     */
    Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
}
