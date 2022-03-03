package com.liyz.auth.service.staff.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "`undo_log`")
public class UndoLogDO implements Serializable {
    private static final long serialVersionUID = -1905530475488684469L;

    @TableId(value = "branch_id", type = IdType.INPUT)
    private Long branchId;

    private String xid;

    private String context;

    private String rollbackInfo;

    private Integer logStatus;

    private Date logCreated;

    private Date logModified;
}
