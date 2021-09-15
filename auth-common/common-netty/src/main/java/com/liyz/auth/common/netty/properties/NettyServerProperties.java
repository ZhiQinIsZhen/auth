package com.liyz.auth.common.netty.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/15 11:23
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "netty.server")
public class NettyServerProperties {

    private int port;

    private String type;

    private String server;

    private boolean heartbeat;

    private boolean enableClientBatchSendRequest;

    private ThreadFactoryProperties threadFactory;
}
