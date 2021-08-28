package com.liyz.auth.open.vo.process;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/28 14:08
 */
@Getter
@Setter
public class ProcessInfoVO implements Serializable {
    private static final long serialVersionUID = 1748963188694537850L;

    /**
     * 流程ID
     */
    @ApiModelProperty(value = "流程ID")
    private String processInstanceId;

    /**
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称")
    private String processInstanceName;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    private String businessKey;

    /**
     * 流程是否结束
     */
    @ApiModelProperty(value = "流程是否结束")
    private Boolean isEnd;

    @ApiModelProperty(value = "变量信息")
    private Map<String,Object> variables;

    /**
     * 任务列表
     */
    @ApiModelProperty(value = "任务列表")
    private List<TaskInfoVO> taskList;
}
