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
 * @date 2020/8/17 11:07
 */
@Getter
@Setter
@TableName(value = "customer")
public class CustomerDO implements Serializable {
    private static final long serialVersionUID = 7001124888527192890L;

    @TableId(value = "customer_id", type = IdType.INPUT)
    private Long customerId;

    private String customerName;

    private String nickName;

    private String password;

    private String mobile;

    private String email;

    private Integer isInactive;

    private Date webTokenTime;

    private Date appTokenTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
