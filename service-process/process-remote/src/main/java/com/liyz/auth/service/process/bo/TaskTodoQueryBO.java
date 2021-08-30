package com.liyz.auth.service.process.bo;

import com.liyz.auth.common.remote.page.PageBaseBO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/30 14:49
 */
@Getter
@Setter
public class TaskTodoQueryBO extends PageBaseBO implements Serializable {
    private static final long serialVersionUID = -1091218469617667788L;

    /**
     * 流程定义key
     */
    private String processDefKey;

    /**
     * 流程实例名称
     */
    private String processName;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 可审批人
     */
    private String assignee;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 角色列表
     */
    private List<String> roleIds;

    /**
     * 参与用户
     */
    private String involvedUser;

    private Boolean finished;
}
