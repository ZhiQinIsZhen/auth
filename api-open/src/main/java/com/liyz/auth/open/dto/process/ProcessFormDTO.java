package com.liyz.auth.open.dto.process;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:26
 */
@Getter
@Setter
public class ProcessFormDTO implements Serializable {
    private static final long serialVersionUID = 3397988724455354887L;

    /**
     * 流程定义key
     */
    @ApiModelProperty(value = "流程定义key")
    @NotBlank(groups = {Start.class}, message = "流程定义key不能为空")
    private String processDefKey;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    @NotBlank(groups = {Start.class}, message = "业务主键不能为空")
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

    public interface Start{}
}
