package com.liyz.auth.common.seata;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/1 14:22
 */
@ConfigurationProperties(prefix = "seata.client")
public class SeataClientProperties implements Serializable {
    private static final long serialVersionUID = 6136969893076042386L;

    /**
     * 应用id
     */
    private String applicationId;

    /**
     * 组
     */
    private String txServiceGroup;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTxServiceGroup() {
        return txServiceGroup;
    }

    public void setTxServiceGroup(String txServiceGroup) {
        this.txServiceGroup = txServiceGroup;
    }
}
