package com.liyz.auth.security.client.config;

import com.liyz.auth.security.client.core.RestfulAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 注释:客户端beans init
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 9:52
 */
@Slf4j
@Configuration
@ComponentScan(value = {"com.liyz.auth.security.client"})
public class SecurityClientBeansConfig {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
        return new RestfulAccessDeniedHandler();
    }
}
