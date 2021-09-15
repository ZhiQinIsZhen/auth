package com.liyz.auth.common.tcp.core.rpc;

import com.liyz.auth.common.tcp.core.protocol.RegisterRMRequest;
import com.liyz.auth.common.tcp.core.protocol.RegisterTMRequest;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:48
 */
public interface RegisterCheckAuthHandler {

    /**
     * Reg transaction manager check auth boolean.
     *
     * @param request the request
     * @return the boolean
     */
    boolean regTransactionManagerCheckAuth(RegisterTMRequest request);

    /**
     * Reg resource manager check auth boolean.
     *
     * @param request the request
     * @return the boolean
     */
    boolean regResourceManagerCheckAuth(RegisterRMRequest request);
}
