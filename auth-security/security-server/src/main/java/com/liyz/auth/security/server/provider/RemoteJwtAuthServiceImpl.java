package com.liyz.auth.security.server.provider;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.security.base.constant.SecurityConstant;
import com.liyz.auth.security.base.constant.SecurityEnum;
import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.remote.RemoteJwtAuthService;
import com.liyz.auth.common.base.util.AuthUser;
import com.liyz.auth.security.base.user.ClaimDetail;
import com.liyz.auth.security.remote.RemoteLoadByUsernameService;
import com.liyz.auth.security.remote.bo.AuthUserBO;
import com.liyz.auth.security.server.parse.JwtAccessTokenParser;
import com.liyz.auth.security.server.util.DubboGenericServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 10:12
 */
@Slf4j
@DubboService
public class RemoteJwtAuthServiceImpl implements RemoteJwtAuthService {

    @Value("${jwt.tokenHeader.head:Bearer }")
    private String tokenHeaderHead;

    @DubboReference(timeout = 5000)
    RemoteGrantedAuthorityService remoteGrantedAuthorityService;

    @Autowired
    JwtAccessTokenParser jwtAccessTokenParser;

    /**
     * 登陆
     *
     * @param username
     * @param audienceType
     * @return
     */
    @Override
    public AuthUser login(final String username, final SecurityEnum.AudienceType audienceType) {
        AuthUserBO authUserBO = null;
        if (StringUtils.isNotBlank(username) && Objects.nonNull(audienceType)) {
            authUserBO = getAuthUserByGenericService("login", username, audienceType.getCode());
        }
        return CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @param audienceType
     * @return
     */
    @Override
    public AuthUser loadUserByUsername(final String username, final SecurityEnum.AudienceType audienceType) {
        AuthUserBO authUserBO = null;
        if (StringUtils.isNotBlank(username) && Objects.nonNull(audienceType)) {
            authUserBO = getAuthUserByGenericService("loadByUsername", username, audienceType.getCode());
        }
        AuthUser authUser = CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
        if (Objects.nonNull(authUserBO) && !CollectionUtils.isEmpty(authUserBO.getRoleIds())) {
            authUser.setAuthorityList(remoteGrantedAuthorityService.getByRoleIds(authUserBO.getRoleIds(), audienceType.getCode()));
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
                AuthUser authUser = loadUserByUsername(
                        claimDetail.getUsername(),
                        SecurityEnum.AudienceType.getByCode(claimDetail.getAudience())
                );
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

    /**
     * 通过 genericService 获取用户信息
     *
     * @param methodName
     * @param o
     * @param group
     * @return
     */
    private AuthUserBO getAuthUserByGenericService(String methodName, Object o, String group) {
        String[] parameterTypes = new String[] {String.class.getName()};
        Map<String, Object> map = (Map<String, Object>) DubboGenericServiceUtil
                .getByClassName(RemoteLoadByUsernameService.class, SecurityConstant.DEFAULT_VERSION, group)
                .$invoke(methodName, parameterTypes, new Object[] {o});
        AuthUserBO authUserBO = null;
        if (!CollectionUtils.isEmpty(map)) {
            try {
                authUserBO = CommonCloneUtil.MapToBean(map, AuthUserBO.class);
            } catch (Exception e) {
                log.error("genericServiceLogin 类型转换 error", e);
            }
        }
        return authUserBO;
    }
}
