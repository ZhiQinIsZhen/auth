package com.liyz.auth.service.process.remote;

import com.liyz.auth.service.process.bo.ProcessFormBO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:17
 */
public interface RemoteProcessService {

    /**
     * 发起流程
     *
     * @param processFormBO
     * @return
     */
    ProcessFormBO startProcess(ProcessFormBO processFormBO);
}
