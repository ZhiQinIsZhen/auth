package com.liyz.auth.service.order.model;

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
 * @date 2021/9/1 15:22
 */
@Getter
@Setter
@TableName(value = "`order`")
public class OrderDO implements Serializable {
    private static final long serialVersionUID = 1391389209423193501L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderCode;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
