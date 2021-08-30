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
 * @date 2021/8/30 13:26
 */
@Getter
@Setter
@TableName("customer_role")
public class CustomerRoleDO implements Serializable {
    private static final long serialVersionUID = 3300045088591150389L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private Integer roleId;

    private Integer isInactive;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
