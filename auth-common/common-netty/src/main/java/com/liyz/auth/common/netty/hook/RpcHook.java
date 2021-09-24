package com.liyz.auth.common.netty.hook;

import com.liyz.auth.common.netty.protocol.RpcMessage;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/22 14:31
 */
public interface RpcHook {

    void doBeforeRequest(String remoteAddr, RpcMessage request);

    void doAfterResponse(String remoteAddr, RpcMessage request, Object response);
}
