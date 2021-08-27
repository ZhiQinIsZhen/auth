package com.liyz.auth.service.process.remote;

import com.liyz.auth.service.process.bo.ProcessDeployBO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:14
 */
public interface RemoteProcessDeployService {

    /**
     * 上传流程图
     *
     * @param processDeployBO
     * @return
     */
    Boolean deploy(ProcessDeployBO processDeployBO);
}
