package com.liyz.auth.open.dto.process;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/28 16:42
 */
@Getter
@Setter
public class TaskSubmitDTO implements Serializable {
    private static final long serialVersionUID = -93173667566537897L;

    /**
     * 流程ID
     */
    @ApiModelProperty(value = "流程ID")
    private String processInstanceId;

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    @NotBlank(groups = {Submit.class}, message = "任务ID不能为空")
    private String taskId;

    /**
     * 是否通过 Y:通过;N:未通过
     */
    @ApiModelProperty(value = "是否通过 Y:通过;N:未通过")
    @NotBlank(groups = {Submit.class}, message = "是否通过不能为空")
    private String isPass;

    /**
     * 审批内容
     */
    @ApiModelProperty(value = "审批内容")
    private String content;

    /**
     * 审批附件
     */
    @ApiModelProperty(value = "审批附件")
    private List<String> files;

    public interface Submit{}
}
