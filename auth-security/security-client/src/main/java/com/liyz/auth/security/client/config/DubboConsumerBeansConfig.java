package com.liyz.auth.security.client.config;

import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.remote.RemoteJwtAuthService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/15 9:49
 */
@Configuration
public class DubboConsumerBeansConfig {

    @DubboReference(version = "1.0.0", timeout = 100000)
    private RemoteJwtAuthService remoteJwtAuthService;
    @DubboReference(version = "1.0.0", timeout = 10000)
    private RemoteGrantedAuthorityService remoteGrantedAuthorityService;

    @Bean
    public RemoteJwtAuthService remoteJwtAuthService() {
        return remoteJwtAuthService;
    }

    @Bean
    public RemoteGrantedAuthorityService remoteGrantedAuthorityService() {
        return remoteGrantedAuthorityService;
    }
}
