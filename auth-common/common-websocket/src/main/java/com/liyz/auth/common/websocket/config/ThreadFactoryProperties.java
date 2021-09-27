package com.liyz.auth.common.websocket.config;

import com.liyz.auth.common.util.JsonMapperUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 10:44
 */
@Getter
@Setter
public class ThreadFactoryProperties {

    private String bossThreadPrefix;

    private String workerThreadPrefix;

    private int bossThreadSize;

    private String workerThreadSize;

    @Override
    public String toString() {
        return JsonMapperUtil.toJSONString(this);
    }
}
