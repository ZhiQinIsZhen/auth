package com.liyz.auth.service.process.remote;

import com.liyz.auth.service.process.bo.ProcessFormBO;
import com.liyz.auth.service.process.bo.ProcessInfoBO;
import com.liyz.auth.service.process.bo.TaskSubmitBO;

import javax.validation.constraints.NotBlank;
import java.util.Map;

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

    /**
     * 提交流程
     *
     * @param taskSubmitBO
     * @return
     */
    boolean submitTask(TaskSubmitBO taskSubmitBO);

    /**
     * 获取流程详情
     *
     * @param processInstanceId
     * @param taskId
     */
    void taskInfo(@NotBlank(message = "流程实例ID不能为空") String processInstanceId, String taskId);

    /**
     * 获取流程信息
     *
     * @param processInstanceId
     * @return
     */
    ProcessInfoBO processInfo(@NotBlank(message = "流程实例ID不能为空") String processInstanceId);

    /**
     * 获取流程所有变量信息
     *
     * @param processInstanceId
     * @return
     */
    Map<String, Object> processVariables(@NotBlank(message = "流程实例ID不能为空") String processInstanceId);

    /**
     * 根据流程定义和业务主键获取最新一条流程详情
     *
     * @param processDefKey
     * @param businessKey
     * @return
     */
    ProcessInfoBO getLastProcessByProcessAndBusiness(String processDefKey, String businessKey);

    /**
     * 删除流程
     *
     * @param processFormBO
     * @return
     */
    Boolean deleteProcess(ProcessFormBO processFormBO);
}
