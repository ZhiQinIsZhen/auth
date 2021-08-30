package com.liyz.auth.service.process.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liyz.auth.common.remote.page.Page;
import com.liyz.auth.common.util.JsonMapperUtil;
import com.liyz.auth.service.process.bo.*;
import com.liyz.auth.service.process.constant.ProcessExceptionCodeEnum;
import com.liyz.auth.service.process.exception.RemoteProcessServiceException;
import com.liyz.auth.service.process.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfoQuery;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;
    @Resource
    private TaskService taskService;
    @Resource
    private FormService formService;

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

    @Override
    public Boolean submitTask(TaskSubmitBO taskSubmitBO) {
        Authentication.setAuthenticatedUserId(taskSubmitBO.getSubmitUser());
        Task task = taskService.createTaskQuery().active().taskId(taskSubmitBO.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new RemoteProcessServiceException(ProcessExceptionCodeEnum.TASK_HAS_SUBMIT);
        }
        List<IdentityLink> links = getIdentityLinksForTask(task.getId());
        if (!CollectionUtils.isEmpty(links)) {
            if (links.stream().filter(link -> link.getUserId().equals(taskSubmitBO.getSubmitUser())).count() == 0) {
                throw new RemoteProcessServiceException(ProcessExceptionCodeEnum.TASK_SUBMIT_NON_RIGHT);
            }
        }
        formService.submitTaskFormData(task.getId(), getBySubmit(taskSubmitBO));
        return Boolean.TRUE;
    }

    /**
     * 流程信息
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public ProcessInfoBO processInfo(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            return null;
        }
        return getByHistoricProcessInstance(historicProcessInstance);
    }

    /**
     * 获取task审批人
     *
     * @param taskId
     * @return
     */
    @Override
    public List<IdentityLink> getIdentityLinksForTask(String taskId) {
        List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
        return links;
    }

    /**
     * 获取流程所有变量信息
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public Map<String, Object> processVariables(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            return null;
        }
        return historicProcessInstance.getProcessVariables();
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
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .processDefinitionKey(processDefKey)
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(0, 1);
        if (CollectionUtils.isEmpty(historicProcessInstances)) {
            return null;
        }
        return getByHistoricProcessInstance(historicProcessInstances.get(0));
    }

    /**
     * 删除流程
     *
     * @param processFormBO
     * @return
     */
    @Override
    public Boolean deleteProcess(ProcessFormBO processFormBO) {
        Authentication.setAuthenticatedUserId(processFormBO.getApplicant());
        runtimeService.deleteProcessInstance(processFormBO.getProcessInstanceId(), processFormBO.getDeleteReason());
        return Boolean.TRUE;
    }

    /**
     * 查询待办列表
     *
     * @param taskTodoQueryBO
     * @return
     */
    @Override
    public Page<TaskTodoBO> todoList(TaskTodoQueryBO taskTodoQueryBO) {
        TaskInfoQuery<TaskQuery, Task> taskInfoQuery = taskService.createTaskQuery().or()
                .taskCandidateOrAssigned(taskTodoQueryBO.getAssignee())
                .taskCandidateGroupIn(taskTodoQueryBO.getRoleIds())
                .endOr()
                .orderByTaskPriority().desc()
                .orderByTaskCreateTime().desc()
                ;
        //设置不显示任务
        /*List<String> notShowCategory = new ArrayList<>();
        notShowCategory.add("NOT_SHOW");
        taskInfoQuery.processCategoryNotIn(notShowCategory);*/
        if (StringUtils.isNotBlank(taskTodoQueryBO.getProcessInstanceId())) {
            taskInfoQuery.processInstanceId(taskTodoQueryBO.getProcessInstanceId());
        }
        if (StringUtils.isNoneBlank(taskTodoQueryBO.getProcessDefKey())) {
            taskInfoQuery.processDefinitionKey(taskTodoQueryBO.getProcessDefKey());
        }
        long totalCount = taskInfoQuery.count();
        if (totalCount == 0) {
            return new Page<>(Lists.newArrayList(), totalCount, 0, taskTodoQueryBO.getPageNum(),
                    taskTodoQueryBO.getPageSize(), Boolean.FALSE);
        }
        long pages = totalCount % taskTodoQueryBO.getPageSize() == 0
                ? (totalCount / taskTodoQueryBO.getPageSize())
                : (totalCount / taskTodoQueryBO.getPageSize() + 1);
        List<Task> taskList = taskInfoQuery.listPage((taskTodoQueryBO.getPageNum() - 1) * taskTodoQueryBO.getPageSize(),
                taskTodoQueryBO.getPageSize());
        if (CollectionUtils.isEmpty(taskList)) {
            return new Page<>(Lists.newArrayList(), totalCount, (int) pages, taskTodoQueryBO.getPageNum(),
                    taskTodoQueryBO.getPageSize(), pages > taskTodoQueryBO.getPageNum());
        }
        List<TaskTodoBO> taskTodoList = Lists.newArrayList();
        taskList.stream().forEach(task -> {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            List<String> variableList = Lists.newArrayList("startUser", "createTime", "VERSION");
            Map<String, Object> processVariablesMap = taskService.getVariables(task.getId(), variableList);
            TaskTodoBO taskTodoBO = new TaskTodoBO();
            taskTodoBO.setTaskId(task.getId());
            taskTodoBO.setProcessInstanceId(task.getProcessInstanceId());
            taskTodoBO.setTaskName(task.getName());
            taskTodoBO.setTaskDefKey(task.getTaskDefinitionKey());
            taskTodoBO.setProcessDefKey(task.getProcessDefinitionId());
            taskTodoBO.setBeginner((String) processVariablesMap.get("startUser"));
            taskTodoBO.setCreateTime(historicProcessInstance.getStartTime());
            taskTodoBO.setVersion(Optional.ofNullable((Integer) processVariablesMap.get("VERSION")).orElse(0));
            taskTodoBO.setProcessName(historicProcessInstance.getName()); // 流程名称
            taskTodoBO.setEnded(Objects.nonNull(historicProcessInstance.getEndTime())); // 流程状态
            taskTodoBO.setBusinessKey(historicProcessInstance.getBusinessKey());
            taskTodoBO.setExecutionId(task.getExecutionId());
            taskTodoBO.setCommonBusVars(processVariablesMap);
            taskTodoList.add(taskTodoBO);
        });
        return new Page<>(taskTodoList, totalCount, (int) pages, taskTodoQueryBO.getPageNum(),
                taskTodoQueryBO.getPageSize(), pages > taskTodoQueryBO.getPageNum());
    }

    /**
     * 办结列表
     *
     * @param taskTodoQueryBO
     * @return
     */
    @Override
    public Page<TaskDoneBO> doneList(TaskTodoQueryBO taskTodoQueryBO) {
        //不显示的流程
        List<String> notShowProcesses = Lists.newArrayList("process");
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().orderByProcessInstanceStartTime()
                .processDefinitionKeyNotIn(notShowProcesses).desc();
        if (StringUtils.isNotBlank(taskTodoQueryBO.getInvolvedUser())) {
            query.involvedUser(taskTodoQueryBO.getInvolvedUser());
        }
        if (Objects.nonNull(taskTodoQueryBO.getFinished())) {
            if (taskTodoQueryBO.getFinished()) {
                query.finished();
            } else {
                query.unfinished();
            }
        }
        if (StringUtils.isNotBlank(taskTodoQueryBO.getProcessInstanceId())) {
            query.processInstanceId(taskTodoQueryBO.getProcessInstanceId());
        }
        if (StringUtils.isNotBlank(taskTodoQueryBO.getProcessName())) {
            query.processInstanceNameLike("%" + taskTodoQueryBO.getProcessName() + "%");
        }
        long totalCount = query.count();
        if (totalCount == 0) {
            return new Page<>(Lists.newArrayList(), totalCount, 0, taskTodoQueryBO.getPageNum(),
                    taskTodoQueryBO.getPageSize(), Boolean.FALSE);
        }
        long pages = totalCount % taskTodoQueryBO.getPageSize() == 0
                ? (totalCount / taskTodoQueryBO.getPageSize())
                : (totalCount / taskTodoQueryBO.getPageSize() + 1);
        List<HistoricProcessInstance> processInstances = query.listPage((taskTodoQueryBO.getPageNum() - 1) * taskTodoQueryBO.getPageSize(),
                taskTodoQueryBO.getPageSize());
        if (CollectionUtils.isEmpty(processInstances)) {
            return new Page<>(Lists.newArrayList(), totalCount, (int) pages, taskTodoQueryBO.getPageNum(),
                    taskTodoQueryBO.getPageSize(), pages > taskTodoQueryBO.getPageNum());
        }
        List<TaskDoneBO> taskDoneList = Lists.newArrayList();
        processInstances.stream().forEach(processInstance -> {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .includeProcessVariables()
                    .singleResult();
            Map<String, Object> processVariablesMap = historicProcessInstance.getProcessVariables();
            TaskDoneBO taskDoneBO = new TaskDoneBO();
            taskDoneBO.setProcessInstanceId(processInstance.getId());
            taskDoneBO.setProcessDefKey(processInstance.getProcessDefinitionId());
            taskDoneBO.setBeginner((String) processVariablesMap.get("startUser"));
            taskDoneBO.setCreateTime(historicProcessInstance.getStartTime());
            taskDoneBO.setVersion(Optional.ofNullable((Integer) processVariablesMap.get("VERSION")).orElse(0));
            taskDoneBO.setProcessName(historicProcessInstance.getName()); // 流程名称
            taskDoneBO.setEnded(Objects.nonNull(processInstance.getEndTime())); // 流程状态
            taskDoneBO.setBusinessKey(historicProcessInstance.getBusinessKey());
            taskDoneBO.setCommonBusVars(processVariablesMap);
            if (Objects.isNull(processInstance.getEndTime())) {
                List<HistoricActivityInstance> instances = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .list();
                instances.stream()
                        .filter(activityInstance -> Objects.isNull(activityInstance.getEndTime()))
                        .findFirst()
                        .ifPresent(activityInstance -> {
                            taskDoneBO.setTaskId(activityInstance.getTaskId());
                            taskDoneBO.setTaskName(activityInstance.getActivityName()); // 任务名称
                            taskDoneBO.setExecutionId(activityInstance.getExecutionId());
                            taskDoneBO.setTaskCreateTime(activityInstance.getStartTime());
                            if (StringUtils.isNotBlank(activityInstance.getTaskId())) {
                                List<IdentityLink> links = getIdentityLinksForTask(activityInstance.getTaskId());
                                if (!CollectionUtils.isEmpty(links)) {
                                    //todo 可以设置审批人
                                }
                            }
                        });
            }
            taskDoneList.add(taskDoneBO);
        });
        return new Page<>(taskDoneList, totalCount, (int) pages, taskTodoQueryBO.getPageNum(),
                taskTodoQueryBO.getPageSize(), pages > taskTodoQueryBO.getPageNum());
    }

    /**
     * 通过历史流程实例获取流程详情
     *
     * @param historicProcessInstance
     * @return
     */
    private ProcessInfoBO getByHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
        ProcessInfoBO processInfoBO = new ProcessInfoBO();
        processInfoBO.setProcessInstanceId(historicProcessInstance.getId());
        processInfoBO.setProcessInstanceName(historicProcessInstance.getName());
        processInfoBO.setBusinessKey(historicProcessInstance.getBusinessKey());
        processInfoBO.setIsEnd(Objects.nonNull(historicProcessInstance.getEndTime()));
        processInfoBO.setVariables(historicProcessInstance.getProcessVariables());
        //查询task列表
        List<HistoricTaskInstance> historicTaskList = historyService
                .createNativeHistoricTaskInstanceQuery()
                .sql("select * from ACT_HI_TASKINST where PROC_INST_ID_ = #{processId} order by START_TIME_")
                .parameter("processId", historicProcessInstance.getId())
                .list();
        if (!CollectionUtils.isEmpty(historicTaskList)) {
            List<TaskInfoBO> taskList = Lists.newArrayList();
            historicTaskList.stream().forEach(historicTask -> {
                TaskInfoBO taskInfoBO = new TaskInfoBO();
                taskInfoBO.setTaskDefKey(historicTask.getTaskDefinitionKey());
                taskInfoBO.setTaskId(historicTask.getId());
                taskInfoBO.setTaskName(historicTask.getName());
                taskInfoBO.setAssignee(historicTask.getAssignee());
                taskInfoBO.setStartTime(historicTask.getStartTime());
                taskInfoBO.setEndTime(historicTask.getEndTime());
                taskInfoBO.setIsEnd(Objects.nonNull(historicTask.getEndTime()));
                taskInfoBO.setDeleteReason(historicTask.getDeleteReason());
                //设置可签署人
                if (!taskInfoBO.getIsEnd()) {
                    List<String> candidates = Lists.newArrayList();
                    List<IdentityLink> links = getIdentityLinksForTask(historicTask.getId());
                    for (IdentityLink identityLink : links) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            candidates.add(identityLink.getUserId());
                        }
                    }
                    taskInfoBO.setCandidates(candidates);
                }
                //设置task变量
                List<HistoricVariableInstance> varList = historyService
                        .createHistoricVariableInstanceQuery()
                        .executionId(historicTask.getExecutionId())
                        .list();
                if (!CollectionUtils.isEmpty(varList)) {
                    Map<String, Object> varMap = Maps.newHashMap();
                    varList.forEach(historicVariableInstance -> {
                        varMap.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
                    });
                    taskInfoBO.setVariables(varMap);
                }
                taskList.add(taskInfoBO);
            });
            processInfoBO.setTaskList(taskList);
        }
        return processInfoBO;
    }

    private Map<String, String> getBySubmit(TaskSubmitBO taskSubmitBO) {
        Map<String, String> map = Maps.newHashMap();
        if (Objects.nonNull(taskSubmitBO)) {
            if (StringUtils.isNotBlank(taskSubmitBO.getIsPass())) {
                map.put("isPass", taskSubmitBO.getIsPass());
            }
            if (StringUtils.isNotBlank(taskSubmitBO.getContent())) {
                map.put("content", taskSubmitBO.getContent());
            }
            if (!CollectionUtils.isEmpty(taskSubmitBO.getFiles())) {
                map.put("files", JsonMapperUtil.toJSONString(taskSubmitBO.getFiles()));
            }
        }
        return map;
    }
}
