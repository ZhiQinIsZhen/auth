package com.liyz.auth.service.process.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/28 14:09
 */
@Getter
@Setter
public class TaskInfoBO implements Serializable {
    private static final long serialVersionUID = 8839175764455089873L;

    /**
     * 流程ID
     */
    private String processInstanceId;

    /**
     * 流程名称
     */
    private String processInstanceName;

    /**
     * 流程定义KEY
     */
    private String processDefKey;

    /**
     * 任务定义KEY
     */
    private String taskDefKey;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 可审批人
     */
    private List<String> candidates;

    /**
     * 审批人,级别比candidates高
     */
    private String assignee;

    /**
     * 任务是否结束
     */
    private Boolean isEnd;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间(即任务完成时间)
     */
    private Date endTime;

    /**
     * 删除原因:complete正常,delete异常
     */
    private String deleteReason;

    /**
     * 变量信息
     */
    private Map<String,Object> variables;
}
