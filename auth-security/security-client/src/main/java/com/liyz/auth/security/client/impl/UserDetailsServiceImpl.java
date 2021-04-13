package com.liyz.auth.security.client.impl;

import com.google.common.collect.Lists;
import com.liyz.auth.security.base.remote.RemoteJwtAuthService;
import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.user.AuthUser;
import com.liyz.auth.security.base.user.GrantedAuthority;
import com.liyz.auth.security.client.AuthContext;
import com.liyz.auth.security.client.AuthGrantedAuthority;
import com.liyz.auth.security.base.user.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 17:05
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${jwt.user.authority:false}")
    private boolean authority;

    @DubboReference(version = "1.0.0", timeout = 100000)
    private RemoteJwtAuthService remoteJwtAuthService;
    @DubboReference(version = "1.0.0")
    private RemoteGrantedAuthorityService remoteGrantedAuthorityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        AuthUser authUser = loadUserByToken(username);
        AuthContext.setAuthUser(authUser);
        return getByAuthUser(authUser);
    }

    /**
     * 根据token获取认证用户信息
     *
     * @param token
     * @return
     */
    private AuthUser loadUserByToken(String token) {
        AuthUser authUser = remoteJwtAuthService.loadUserByToken(token);
        if (Objects.isNull(authUser)) {
            throw new UsernameNotFoundException("No user found with token !");
        }
        if (authority) {
            List<GrantedAuthority> boList = remoteGrantedAuthorityService.getByRoleId(authUser.getRoleId());
            List<AuthGrantedAuthority> authorityList = new ArrayList<>(boList.size());
            boList.stream().forEach(grantedAuthorityBO -> {
                authorityList.add(new AuthGrantedAuthority(grantedAuthorityBO.getPermissionUrl(), grantedAuthorityBO.getMethod()));
            });
            authUser.setAuthorityList(boList);
        }
        return authUser;
    }

    /**
     * 转化
     *
     * @param authUser
     * @return
     */
    public static AuthUserDetails getByAuthUser(AuthUser authUser) {
        if (Objects.isNull(authUser)) {
            return null;
        }
        List<AuthGrantedAuthority> authorityList;
        if (!CollectionUtils.isEmpty(authUser.getAuthorityList())) {
            authorityList = new ArrayList<>(authUser.getAuthorityList().size());
            for (GrantedAuthority grantedAuthority : authUser.getAuthorityList()) {
                authorityList.add(new AuthGrantedAuthority(grantedAuthority.getPermissionUrl(), grantedAuthority.getMethod()));
            }
        } else {
            authorityList = Lists.newArrayList();
        }
        return new AuthUserDetails(
                authUser.getUserId(),
                authUser.getRoleId(),
                authUser.getLoginName(),
                authUser.getLoginPwd(),
                authUser.getMobile(),
                authUser.getEmail(),
                authorityList,
                authUser.getWebTokenTime(),
                authUser.getAppTokenTime());
    }
}
