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
 * @date 2021/8/27 15:06
 */
@Getter
@Setter
public class ProcessQuery implements Serializable {
    private static final long serialVersionUID = 6173882623111881868L;

    private String processId;

    private String processName;

    private String processDefKey;

    private String memberVipCode;

    private String companyName;

    private String businessKey;

    private Date startTimeLt;

    private Date startTimeGt;

    private Boolean end;
}
