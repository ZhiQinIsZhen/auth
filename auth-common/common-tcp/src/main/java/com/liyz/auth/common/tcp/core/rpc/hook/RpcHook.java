package com.liyz.auth.common.tcp.core.rpc.hook;

import com.liyz.auth.common.tcp.core.protocol.RpcMessage;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 16:01
 */
public interface RpcHook {

    void doBeforeRequest(String remoteAddr, RpcMessage request);

    void doAfterResponse(String remoteAddr, RpcMessage request, Object response);
}
