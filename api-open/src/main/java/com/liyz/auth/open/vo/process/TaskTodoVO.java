package com.liyz.auth.open.vo.process;

import io.swagger.annotations.ApiModelProperty;
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
public class TaskTodoVO implements Serializable {
    private static final long serialVersionUID = -1091218469617667788L;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    private String businessKey;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    /**
     * 流程定义key
     */
    @ApiModelProperty(value = "流程定义key")
    private String processDefKey;

    /**
     * 流程实例名称
     */
    @ApiModelProperty(value = "流程实例名称")
    private String processName;
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 当前节点的taskId
     */
    @ApiModelProperty(value = "当前节点的taskId")
    private String taskId;
    /**
     * 当前流程状态
     */
    @ApiModelProperty(value = "当前流程状态")
    private boolean ended;
    /**
     * 发起人
     */
    @ApiModelProperty(value = "发起人")
    private String beginner;

    /**
     * 发起人中文名
     */
    @ApiModelProperty(value = "发起人中文名")
    private String beginnerChName;

    /**
     * The date/time when this process was created
     */
    @ApiModelProperty(value = "流程创建时间")
    private Date createTime;
    /**
     * 流程版本
     */
    @ApiModelProperty(value = "流程版本")
    private Integer version;
    /**
     * 历时
     */
    @ApiModelProperty(value = "历时")
    private String lastTime;

    /**
     * executionId
     */
    @ApiModelProperty(value = "executionId")
    private String executionId;

    /**
     * 流程节点定义key
     */
    @ApiModelProperty(value = "流程节点定义key")
    private String taskDefKey;

    /**
     * 基础业务属性值
     */
    @ApiModelProperty(value = "基础业务属性值")
    private Map<String, Object> commonBusVars;

    /**
     * 流程节点审批人名称  多个人用中文逗号隔开
     */
    @ApiModelProperty(value = "流程节点审批人名称")
    private String nodeAssigneeName;

    /**
     * 流程节点审批人名称 第一个名称  后加“等”字
     */
    @ApiModelProperty(value = "流程节点审批人名称")
    private String firstAssigneeName;
}
