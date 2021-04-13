package com.liyz.auth.security.base.user;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 16:10
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 6363286926342633192L;

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

    private List<GrantedAuthority> authorityList;
}
