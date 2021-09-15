package com.liyz.auth.common.tcp.registry;

import com.liyz.auth.common.tcp.loader.EnhancedServiceLoader;
import com.liyz.auth.common.remote.exception.RemoteServiceException;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:56
 */
public class RegistryFactory {

    private static volatile RegistryService instance = null;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RegistryService getInstance() {
        if (instance == null) {
            synchronized (RegistryFactory.class) {
                if (instance == null) {
                    instance = buildRegistryService();
                }
            }
        }
        return instance;
    }

    private static RegistryService buildRegistryService() {
        RegistryType registryType;
        String registryTypeName = "file";
        try {
            registryType = RegistryType.getType(registryTypeName);
        } catch (Exception exx) {
            throw new RemoteServiceException("not support registry type: " + registryTypeName);
        }
        if (RegistryType.File == registryType) {
            return FileRegistryServiceImpl.getInstance();
        } else {
            return EnhancedServiceLoader.load(RegistryProvider.class, Objects.requireNonNull(registryType).name()).provide();
        }
    }
}
