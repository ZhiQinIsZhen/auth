package com.liyz.auth.service.process.provider;

import com.liyz.auth.service.process.bo.ProcessFormBO;
import com.liyz.auth.service.process.remote.RemoteProcessService;
import com.liyz.auth.service.process.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:17
 */
@Slf4j
@DubboService
public class RemoteProcessServiceImpl implements RemoteProcessService {

    @Resource
    private ProcessService processService;

    /**
     * 发起流程
     *
     * @param processFormBO
     * @return
     */
    @Override
    public ProcessFormBO startProcess(ProcessFormBO processFormBO) {
        processFormBO.setProcessInstanceId(processService.startProcess(processFormBO));
        return processFormBO;
    }
}
