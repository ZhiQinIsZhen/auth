package com.liyz.auth.common.netty.hook;

import com.liyz.auth.common.netty.core.RpcStatus;
import com.liyz.auth.common.netty.protocol.RpcMessage;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 20:36
 */
public class StatusRpcHook implements RpcHook {

    @Override
    public void doBeforeRequest(String remoteAddr, RpcMessage request) {
        RpcStatus.beginCount(remoteAddr);
    }

    @Override
    public void doAfterResponse(String remoteAddr, RpcMessage request, Object response) {
        RpcStatus.endCount(remoteAddr);
    }
}
