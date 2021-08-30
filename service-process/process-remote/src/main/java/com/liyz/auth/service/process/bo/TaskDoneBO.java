package com.liyz.auth.service.process.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/30 15:11
 */
@Getter
@Setter
public class TaskDoneBO implements Serializable {
    private static final long serialVersionUID = -1091218469617667788L;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程定义key
     */
    private String processDefKey;

    /**
     * 流程实例名称
     */
    private String processName;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 当前节点的taskId
     */
    private String taskId;

    /**
     * 任务节点创建时间
     */
    private Date taskCreateTime;

    /**
     * 当前流程状态
     */
    private boolean ended;
    /**
     * 发起人
     */
    private String beginner;

    /**
     * 发起人中文名
     */
    private String beginnerChName;

    /**
     * The date/time when this process was created
     */
    private Date createTime;
    /**
     * 流程版本
     */
    private Integer version;
    /**
     * 历时
     */
    private String lastTime;

    /**
     * excecutionId
     */
    private String executionId;

    /**
     * 流程节点定义key
     */
    private String taskDefKey;

    /**
     * 基础业务属性值
     */
    private Map<String, Object> commonBusVars;
}
