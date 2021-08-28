package com.liyz.auth.service.process.bo;

import lombok.Getter;
import lombok.Setter;

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
public class TaskSubmitBO implements Serializable {
    private static final long serialVersionUID = -93173667566537897L;

    /**
     * 流程ID
     */
    private String processInstanceId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 是否通过 Y:通过;N:未通过
     */
    private String isPass;

    /**
     * 审批内容
     */
    private String content;

    /**
     * 审批附件
     */
    private List<String> files;

    /**
     * 审批人
     */
    private String submitUser;
}
