package com.liyz.auth.common.tcp.registry;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:58
 */
public interface RegistryProvider {

    /**
     * provide a registry implementation instance
     * @return RegistryService
     */
    RegistryService provide();
}
