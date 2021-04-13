package com.liyz.auth.security.remote.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 14:53
 */
@Getter
@Setter
public class AuthUserBO implements Serializable {
    private static final long serialVersionUID = -7254035773562123155L;

    private Long userId;

    private String loginName;

    private String nickName;

    private String userName;

    private Integer roleId;

    private String mobile;

    private String email;

    private String loginPwd;

    private Date regTime;

    private Date webTokenTime;

    private Date appTokenTime;

    private List<GrantedAuthorityBO> authorityList;
}
