package com.liyz.auth.common.websocket.config;

import com.liyz.auth.common.util.JsonMapperUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 10:34
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.websocket.server")
public class WebsocketProperties {

    private int port;

    private String socketPath;

    private String type;

    private boolean heartbeat;

    private int shutdownWait;

    private ThreadFactoryProperties threadFactory;

    @Override
    public String toString() {
        return JsonMapperUtil.toJSONString(this);
    }
}
