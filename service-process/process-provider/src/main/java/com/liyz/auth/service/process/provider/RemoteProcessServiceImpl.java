package com.liyz.auth.service.process.provider;

import com.liyz.auth.service.process.bo.ProcessFormBO;
import com.liyz.auth.service.process.bo.ProcessInfoBO;
import com.liyz.auth.service.process.remote.RemoteProcessService;
import com.liyz.auth.service.process.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Map;

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

    @Override
    public boolean submitTask() {
        return false;
    }

    /**
     * 获取流程详情
     *
     * @param processInstanceId
     * @param taskId
     */
    @Override
    public void taskInfo(String processInstanceId, String taskId) {

    }

    /**
     * 获取流程信息
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public ProcessInfoBO processInfo(String processInstanceId) {
        return processService.processInfo(processInstanceId);
    }

    /**
     * 获取流程所有变量信息
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public Map<String, Object> processVariables(String processInstanceId) {
        return processService.processVariables(processInstanceId);
    }

    /**
     * 根据流程定义和业务主键获取最新一条流程详情
     *
     * @param processDefKey
     * @param businessKey
     * @return
     */
    @Override
    public ProcessInfoBO getLastProcessByProcessAndBusiness(String processDefKey, String businessKey) {
        return processService.getLastProcessByProcessAndBusiness(processDefKey, businessKey);
    }

    /**
     * 删除流程
     *
     * @param processFormBO
     * @return
     */
    @Override
    public Boolean deleteProcess(ProcessFormBO processFormBO) {
        return processService.deleteProcess(processFormBO);
    }


}
