package com.liyz.auth.security.base.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 17:08
 */
@Getter
@Setter
public class AuthUserDetails extends User {

    /**
     * 用户id
     */
    private final Long id;

    /**
     * 用户角色
     */
    private final Integer roleId;

    /**
     * 用户邮箱
     */
    private final String email;

    /**
     * 用户手机
     */
    private final String mobile;

    private final String loginName;

    private final String nikeName;

    /**
     * 最新的web登陆时间
     */
    private final Date lastWebPasswordResetDate;

    /**
     * 最新的app登陆时间
     */
    private final Date lastAppPasswordResetDate;

    public AuthUserDetails(Long id, Integer roleId, String username, String password, String mobile, String email, String loginName,
                           String nikeName, Collection<? extends GrantedAuthority> authorities, Date lastWebPasswordResetDate,
                           Date lastAppPasswordResetDate) {
        super(username, password, authorities);
        this.id = id;
        this.roleId = roleId;
        this.mobile = mobile;
        this.email = email;
        this.loginName = loginName;
        this.nikeName = nikeName;
        this.lastWebPasswordResetDate = lastWebPasswordResetDate;
        this.lastAppPasswordResetDate = lastAppPasswordResetDate;
    }

    public AuthUserDetails(Long id, Integer roleId, String username, String password, String mobile, String email, String loginName,
                            boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                            String nikeName, Collection<? extends GrantedAuthority> authorities, Date lastWebPasswordResetDate,
                            Date lastAppPasswordResetDate) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.roleId = roleId;
        this.mobile = mobile;
        this.email = email;
        this.loginName = loginName;
        this.nikeName = nikeName;
        this.lastWebPasswordResetDate = lastWebPasswordResetDate;
        this.lastAppPasswordResetDate = lastAppPasswordResetDate;
    }
}
