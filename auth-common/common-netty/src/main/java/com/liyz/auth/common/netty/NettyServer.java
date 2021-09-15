package com.liyz.auth.common.netty;

import com.liyz.auth.common.netty.properties.NettyServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/15 11:20
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({NettyServerProperties.class})
public class NettyServer {

    private NettyServerProperties nettyServerProperties;

    public NettyServer(NettyServerProperties nettyServerProperties) {
        this.nettyServerProperties = nettyServerProperties;
    }


}
