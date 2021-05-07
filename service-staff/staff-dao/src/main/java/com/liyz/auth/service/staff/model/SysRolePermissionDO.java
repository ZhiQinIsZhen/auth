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
 * @date 2020/8/17 11:28
 */
@Getter
@Setter
@TableName(value = "sys_role_permission")
public class SysRolePermissionDO implements Serializable {
    private static final long serialVersionUID = 5933193179821183888L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    private Integer permissionId;

    private Integer isInactive;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
