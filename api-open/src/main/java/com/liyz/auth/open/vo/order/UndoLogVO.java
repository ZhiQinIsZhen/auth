package com.liyz.auth.open.vo.order;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/2 17:14
 */
@Getter
@Setter
public class UndoLogVO implements Serializable {
    private static final long serialVersionUID = -1905530475488684469L;

    private Long branchId;

    private String xid;

    private String context;

    private String rollbackInfo;

    private Integer logStatus;

    private Date logCreated;

    private Date logModified;
}
