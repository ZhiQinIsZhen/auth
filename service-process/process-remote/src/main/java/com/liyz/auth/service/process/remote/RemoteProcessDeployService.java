package com.liyz.auth.service.process.remote;

import com.liyz.auth.common.remote.page.Page;
import com.liyz.auth.service.process.bo.ProcessDefinitionBO;
import com.liyz.auth.service.process.bo.ProcessDeployBO;

import java.util.List;

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

    /**
     * 查询列表
     */
    List<ProcessDefinitionBO> deployList();

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    Page<ProcessDefinitionBO> deployPage(Integer page, Integer size);
}
