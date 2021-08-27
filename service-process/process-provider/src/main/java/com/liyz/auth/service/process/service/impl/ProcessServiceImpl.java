package com.liyz.auth.service.process.service.impl;

import com.liyz.auth.service.process.bo.ProcessFormBO;
import com.liyz.auth.service.process.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:32
 */
@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 发起流程
     *
     * @param processFormBO
     * @return
     */
    @Override
    public String startProcess(ProcessFormBO processFormBO) {
        Authentication.setAuthenticatedUserId(processFormBO.getApplicant());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processFormBO.getProcessDefKey(), processFormBO.getBusinessKey());
        return processInstance.getProcessInstanceId();
    }
}
