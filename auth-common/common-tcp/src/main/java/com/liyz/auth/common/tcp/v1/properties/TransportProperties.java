package com.liyz.auth.common.tcp.v1.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 14:20
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "transport")
public class TransportProperties {

    private int port;

    private String type;

    private String server;

    private boolean heartbeat;

    private boolean enableClientBatchSendRequest;

    private ThreadFactoryProperties threadFactory;
}
