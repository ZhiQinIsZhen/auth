package com.liyz.auth.common.tcp.config;

import java.util.Set;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 14:13
 */
public class SystemConfig extends AbstractConfiguration{

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public boolean putConfig(String dataId, String content, long timeoutMills) {
        return false;
    }

    @Override
    public String getLatestConfig(String dataId, String defaultValue, long timeoutMills) {
        return null;
    }

    @Override
    public boolean putConfigIfAbsent(String dataId, String content, long timeoutMills) {
        return false;
    }

    @Override
    public boolean removeConfig(String dataId, long timeoutMills) {
        return false;
    }

    @Override
    public void addConfigListener(String dataId, ConfigurationChangeListener listener) {

    }

    @Override
    public void removeConfigListener(String dataId, ConfigurationChangeListener listener) {

    }

    @Override
    public Set<ConfigurationChangeListener> getConfigListeners(String dataId) {
        return null;
    }
}
