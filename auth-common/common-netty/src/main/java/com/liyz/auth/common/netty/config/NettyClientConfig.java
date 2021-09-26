package com.liyz.auth.common.netty.config;

import com.liyz.auth.common.netty.constant.NettyServerConstant;
import com.liyz.auth.common.netty.constant.NettyServerType;
import com.liyz.auth.common.netty.properties.NettyServerProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 15:49
 */
public class NettyClientConfig extends NettyBaseConfig {

    @Getter
    @Setter
    private int connectTimeoutMillis = 10000;
    @Getter
    @Setter
    private int clientSocketSndBufSize = 153600;
    @Getter
    @Setter
    private int clientSocketRcvBufSize = 153600;

    public NettyClientConfig(NettyServerProperties nettyServerProperties) {
        super(nettyServerProperties);
    }

    /**
     * Get client selector thread prefix string.
     *
     * @return the string
     */
    public String getClientSelectorThreadPrefix() {
        String prefix = nettyServerProperties.getThreadFactory().getClientSelectorThreadPrefix();
        return StringUtils.isBlank(prefix) ? NettyServerConstant.DEFAULT_SELECTOR_THREAD_PREFIX : prefix;
    }

    /**
     * Get client worker thread prefix string.
     *
     * @return the string
     */
    public String getClientWorkerThreadPrefix() {
        String prefix = nettyServerProperties.getThreadFactory().getClientWorkerThreadPrefix();
        return StringUtils.isBlank(prefix) ? NettyServerConstant.DEFAULT_WORKER_THREAD_PREFIX : prefix;
    }

    public int getClientWorkerThreads() {
        return workerThreadSize;
    }

    /**
     * Enable native boolean.
     *
     * @return the boolean
     */
    public boolean enableNative() {
        return nettyServerType == NettyServerType.NATIVE;
    }

    /**
     * Gets channel max read idle seconds.
     *
     * @return the channel max read idle seconds
     */
    public int getChannelMaxReadIdleSeconds() {
        return maxReadIdleSeconds;
    }

    /**
     * Gets client channel max idle time seconds.
     *
     * @return the client channel max idle time seconds
     */
    public int getChannelMaxWriteIdleSeconds() {
        return maxWriteIdleSeconds;
    }

    /**
     * Gets channel max all idle seconds.
     *
     * @return the channel max all idle seconds
     */
    public int getChannelMaxAllIdleSeconds() {
        return 0;
    }

    public boolean isEnableClientBatchSendRequest() {
        return nettyServerProperties.isEnableClientBatchSendRequest();
    }
}
