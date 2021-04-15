package com.liyz.auth.security.base.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/15 15:14
 */
@Getter
@Setter
public class ClaimDetail implements Serializable {
    private static final long serialVersionUID = 5860773039021614254L;

    private String username;

    private Long userId;

    private Integer device;

    private Date creation;

    private Date expiration;
}
