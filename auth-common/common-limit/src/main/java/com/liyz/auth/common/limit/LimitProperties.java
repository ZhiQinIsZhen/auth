package com.liyz.auth.common.limit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 17:05
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "request.limit")
public class LimitProperties {

    private boolean enable = false;

    private double totalCount = 1.0;

    private String model = "caffeine";
}
