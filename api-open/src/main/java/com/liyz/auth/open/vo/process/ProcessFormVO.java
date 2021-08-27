package com.liyz.auth.open.vo.process;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:20
 */
@Getter
@Setter
public class ProcessFormVO implements Serializable {
    private static final long serialVersionUID = -3405324913451984730L;

    /**
     * 流程定义key
     */
    @ApiModelProperty(value = "流程定义key")
    private String processDefKey;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    private String businessKey;

    /**
     * 发起人
     */
    @ApiModelProperty(value = "发起人")
    private String applicant;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;
}
