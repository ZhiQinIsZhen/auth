package com.liyz.auth.service.staff.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/8/17 11:21
 */
@Getter
@Setter
@TableName(value = "sys_permission")
public class SysPermissionDO implements Serializable {
    private static final long serialVersionUID = 2471090246732817951L;

    @TableId(value = "permission_id", type = IdType.INPUT)
    private Integer permissionId;

    private String permissionUrl;

    private String method;

    private String permissionName;

    private Integer isInactive;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
