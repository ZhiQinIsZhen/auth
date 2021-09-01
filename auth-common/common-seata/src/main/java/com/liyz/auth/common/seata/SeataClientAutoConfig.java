package com.liyz.auth.common.seata;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/1 14:31
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "seata", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SeataClientProperties.class})
public class SeataClientAutoConfig {

    @Bean
    public GlobalTransactionScanner globalTransactionScanner(SeataClientProperties seataClientProperties){
        return new GlobalTransactionScanner(seataClientProperties.getApplicationId(), seataClientProperties.getTxServiceGroup());
    }
}
