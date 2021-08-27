package com.liyz.auth.service.process.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 15:05
 */
@Getter
@Setter
public class ProcessDO implements Serializable {
    private static final long serialVersionUID = -5521273122195581279L;

    private String processName;
    private String processId;
    private String processDefKey;
    private String businessKey;
    private String startUserId;
    private Date startTime;
    private Date endTime;
    private String taskName;
    private String taskId;
    private Date taskCreateTime;
    private String companyName;
    private String companyId;
    private String vipLevelCode;
}
