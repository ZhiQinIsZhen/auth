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
 * @date 2021/8/27 16:08
 */
@Getter
@Setter
public class ProcessDefinitionVO implements Serializable {
    private static final long serialVersionUID = -281105883862896490L;

    @ApiModelProperty(value = "ID", example = "TEST_LISTENER:1:1683570125619200")
    private String id;

    @ApiModelProperty(value = "类别", example = "http://www.activiti.org/test")
    private String category;

    @ApiModelProperty(value = "流程名称", example = "My process")
    private String name;

    @ApiModelProperty(value = "流程定义key", example = "TEST_LISTENER")
    private String key;

    @ApiModelProperty(value = "描述", example = "我的第一个流程")
    private String description;

    @ApiModelProperty(value = "deploy ID", example = "1683570059296768")
    private int version;

    @ApiModelProperty(value = "流程图名称", example = "TEST_LISTENER.bpmn")
    private String resourceName;

    @ApiModelProperty(value = "deployment ID", example = "TEST_LISTENER:1:1683570125619200")
    private String deploymentId;

    @ApiModelProperty(value = "资源名称", example = "TEST_LISTENER.TEST_LISTENER.png")
    String diagramResourceName;

    @ApiModelProperty(value = "startFormKey", example = "false")
    private boolean startFormKey;

    @ApiModelProperty(value = "isGraphicalNotationDefined", example = "true")
    boolean isGraphicalNotationDefined;

    @ApiModelProperty(value = "isSuspended", example = "false")
    boolean isSuspended;

    @ApiModelProperty(value = "租户ID", example = "CN1683570125619200")
    private String tenantId;
}
