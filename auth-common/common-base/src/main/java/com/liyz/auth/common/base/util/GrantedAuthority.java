package com.liyz.auth.common.base.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 16:19
 */
@Getter
@Setter
public class GrantedAuthority implements Serializable {
    private static final long serialVersionUID = -6387917568457384412L;

    private Integer roleId;

    private Integer permissionId;

    private String permissionUrl;

    private String method;
}
