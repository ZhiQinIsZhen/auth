package com.liyz.auth.common.limit.auto;

import com.liyz.auth.common.limit.LimitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 17:11
 */
@Configuration
@ConditionalOnProperty(prefix = "request.limit", name = "enable", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({LimitProperties.class})
public class LimitAutoConfiguration {

    private LimitProperties limitProperties;

    public LimitAutoConfiguration(LimitProperties limitProperties) {
        this.limitProperties = limitProperties;
        System.setProperty("request.limit.totalCount", String.valueOf(limitProperties.getTotalCount()));
    }
}
