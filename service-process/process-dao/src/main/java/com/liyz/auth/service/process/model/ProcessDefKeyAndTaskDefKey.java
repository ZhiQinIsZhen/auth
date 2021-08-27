package com.liyz.auth.service.process.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 15:02
 */
@Getter
@Setter
public class ProcessDefKeyAndTaskDefKey implements Serializable {
    private static final long serialVersionUID = 6938335130713786846L;

    /**
     * 流程的定义key
     * @NotBlank
     */
    private String processDefinitionKey;

    /**
     * 任务的定义key
     * 当流程定义key为空时任务定义key无效
     */
    private String taskDefinitionKey;
}
