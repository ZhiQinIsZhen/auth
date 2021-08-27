package com.liyz.auth.service.process.service;

import com.liyz.auth.service.process.bo.ProcessFormBO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:31
 */
public interface ProcessService {

    /**
     * 发起流程
     *
     * @param processFormBO
     * @return
     */
    String startProcess(ProcessFormBO processFormBO);
}
