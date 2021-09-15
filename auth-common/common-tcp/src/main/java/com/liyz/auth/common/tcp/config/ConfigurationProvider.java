package com.liyz.auth.common.tcp.config;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:04
 */
public interface ConfigurationProvider {

    /**
     * provide a AbstractConfiguration implementation instance
     * @return Configuration
     */
    Configuration provide();
}
