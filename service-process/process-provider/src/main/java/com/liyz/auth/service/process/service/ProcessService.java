package com.liyz.auth.service.process.service;

import com.liyz.auth.common.remote.page.Page;
import com.liyz.auth.service.process.bo.*;
import org.activiti.engine.task.IdentityLink;

import java.util.List;
import java.util.Map;

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

    /**
     * 提交流程
     *
     * @param taskSubmitBO
     * @return
     */
    Boolean submitTask(TaskSubmitBO taskSubmitBO);

    /**
     * 流程信息
     *
     * @param processInstanceId
     * @return
     */
    ProcessInfoBO processInfo(String processInstanceId);

    /**
     * 获取task审批人
     *
     * @param taskId
     * @return
     */
    List<IdentityLink> getIdentityLinksForTask(String taskId);

    /**
     * 获取流程所有变量信息
     *
     * @param processInstanceId
     * @return
     */
    Map<String, Object> processVariables(String processInstanceId);

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

    /**
     * 查询待办列表
     *
     * @param taskTodoQueryBO
     * @return
     */
    Page<TaskTodoBO> todoList(TaskTodoQueryBO taskTodoQueryBO);

    /**
     * 办结列表
     *
     * @param taskTodoQueryBO
     * @return
     */
    Page<TaskDoneBO> doneList(TaskTodoQueryBO taskTodoQueryBO);
}
