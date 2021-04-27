package com.liyz.auth.security.client.impl;

import com.google.common.collect.Lists;
import com.liyz.auth.security.base.user.AuthUser;
import com.liyz.auth.security.base.user.AuthUserDetails;
import com.liyz.auth.security.base.user.GrantedAuthority;
import com.liyz.auth.security.client.AuthGrantedAuthority;
import com.liyz.auth.security.client.context.JwtContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@Configuration
@ConditionalOnBean(value = {AuthenticationManager.class})
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${jwt.user.authority:false}")
    private boolean authority;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        AuthUser authUser = getUserByUsername(username);
        UserDetails userDetails = getByAuthUser(authUser);
        return userDetails;
    }

    /**
     * 根据username获取认证用户信息
     *
     * @param username
     * @return
     */
    private AuthUser getUserByUsername(String username) {
        AuthUser authUser = JwtContextHolder.getJwtAuthService().login(username);
        if (Objects.isNull(authUser)) {
            throw new UsernameNotFoundException("No user found with token !");
        }
        if (authority) {
            List<GrantedAuthority> boList = JwtContextHolder.getGrantedAuthorityService().getByRoleId(authUser.getRoleId());
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
                authUser.getUserName(),
                authUser.getLoginPwd(),
                authUser.getMobile(),
                authUser.getEmail(),
                authUser.getLoginName(),
                authUser.getNickName(),
                authorityList,
                authUser.getWebTokenTime(),
                authUser.getAppTokenTime());
    }
}
