package com.liyz.auth.service.staff.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liyz.auth.common.dao.annotation.Desensitization;
import com.liyz.auth.common.dao.annotation.EncryptDecrypt;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/24 14:51
 */
@Getter
@Setter
@TableName(value = "rule_log")
public class RuleLogDO implements Serializable {
    private static final long serialVersionUID = 3184537555605762172L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Desensitization
    private String name;

    @EncryptDecrypt
    private String pwd;
}
