package com.liyz.auth.security.server.impl;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.security.base.constant.SecurityEnum;
import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.remote.RemoteJwtAuthService;
import com.liyz.auth.security.base.user.AuthUser;
import com.liyz.auth.security.base.user.ClaimDetail;
import com.liyz.auth.security.remote.RemoteLoadByUsernameService;
import com.liyz.auth.security.remote.bo.AuthUserBO;
import com.liyz.auth.security.remote.bo.GrantedAuthorityBO;
import com.liyz.auth.security.server.parse.JwtAccessTokenParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 10:12
 */
@DubboService(version = "1.0.0")
public class RemoteJwtAuthServiceImpl implements RemoteJwtAuthService {

    @Value("${jwt.tokenHeader.head:Bearer }")
    private String tokenHeaderHead;

    @DubboReference(version = "1.0.0", timeout = 5000)
    RemoteGrantedAuthorityService remoteGrantedAuthorityService;
    @DubboReference(version = "1.0.0", timeout = 5000, group = "member")
    RemoteLoadByUsernameService remoteLoadByUsernameService;
    @DubboReference(version = "1.0.0", timeout = 5000, group = "staff")
    RemoteLoadByUsernameService staffUsernameService;

    @Autowired
    JwtAccessTokenParser jwtAccessTokenParser;

    @Override
    public AuthUser login(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        AuthUserBO authUserBO = remoteLoadByUsernameService.login(username);
        return CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
    }

    @Override
    public AuthUser loadUserByUsername(final String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        AuthUserBO authUserBO = remoteLoadByUsernameService.loadByUsername(username);
        return CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
    }

    @Override
    public AuthUser loginByStaff(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        AuthUserBO authUserBO = staffUsernameService.login(username);
        return CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
    }

    @Override
    public AuthUser loadUserByUsernameByStaff(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        AuthUserBO authUserBO = staffUsernameService.loadByUsername(username);
        AuthUser authUser = CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
        if (Objects.nonNull(authUser) && Objects.nonNull(authUser.getRoleId())) {
            authUser.setAuthorityList(remoteGrantedAuthorityService.getByRoleId(authUserBO.getRoleId()));
        }
        return authUser;
    }

    @Override
    public AuthUser loadUserByToken(final String token) {
        if (StringUtils.isNotBlank(token)) {
            if (token.startsWith(tokenHeaderHead)) {
                final String authToken = token.substring(tokenHeaderHead.length()).trim();
                if (jwtAccessTokenParser.isTokenExpired(authToken)) {
                    throw new RemoteServiceException(CommonExceptionCodeEnum.AuthorizationTimeOut);
                }
                ClaimDetail claimDetail = jwtAccessTokenParser.getDetailByToken(authToken);
                AuthUser authUser = null;
                if (SecurityEnum.AudienceType.Member.getCode().equals(claimDetail.getAudience())) {
                    authUser = loadUserByUsername(claimDetail.getUsername());
                } else {
                    authUser = loadUserByUsernameByStaff(claimDetail.getUsername());
                }
                if (jwtAccessTokenParser.getCreationByToken(authToken).compareTo(authUser.getWebTokenTime()) != 0) {
                    throw new RemoteServiceException(CommonExceptionCodeEnum.OthersLogin);
                }
                return authUser;
            }
        }
        return null;
    }

    @Override
    public String getJWT(final ClaimDetail claimDetail) {
        return jwtAccessTokenParser.generateToken(claimDetail);
    }

    @Override
    public Integer validateToken(String token, UserDetails userDetails, Integer device) {
        return null;
    }
}
