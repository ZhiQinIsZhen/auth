package com.liyz.auth.open.dto.process;

import com.liyz.auth.open.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/30 18:00
 */
@Getter
@Setter
public class TaskTodoQueryDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = -5513909854990023009L;

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
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    private String businessKey;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程ID")
    private String processInstanceId;

    /**
     * 参与用户
     */
    @ApiModelProperty(value = "参与用户")
    private String involvedUser;

    @ApiModelProperty(value = "是否结束")
    private Boolean finished;
}
