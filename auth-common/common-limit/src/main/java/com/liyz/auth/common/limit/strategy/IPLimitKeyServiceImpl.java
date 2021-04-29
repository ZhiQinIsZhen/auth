package com.liyz.auth.common.limit.strategy;

import com.liyz.auth.common.base.util.HttpRequestUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/29 14:07
 */
@Slf4j
public class IPLimitKeyServiceImpl implements LimitKeyService{

    @Override
    public String getKey(Limit limit) {
        String ip = HttpRequestUtil.getIpAddress();
        log.info("ip:{}", RpcContext.getContext().getRemoteHost());
        return ip;
    }

    @Override
    public double getTotalCount(Limit limit) {
        return limit.count();
    }
}
