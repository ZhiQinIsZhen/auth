package com.liyz.auth.security.server.impl;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.security.base.remote.RemoteJwtAuthService;
import com.liyz.auth.security.base.user.AuthUser;
import com.liyz.auth.security.remote.RemoteLoadByUsernameService;
import com.liyz.auth.security.remote.bo.AuthUserBO;
import com.liyz.auth.security.server.parse.JwtAccessTokenParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;

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

    @DubboReference(version = "1.0.0")
    RemoteLoadByUsernameService remoteLoadByUsernameService;

    @Autowired
    JwtAccessTokenParser jwtAccessTokenParser;

    @Override
    public AuthUser loadUserByToken(final String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (token.startsWith(tokenHeaderHead)) {
            final String authToken = token.substring(tokenHeaderHead.length()).trim();
            final String username = jwtAccessTokenParser.getUsernameFromToken(authToken);
            AuthUserBO authUserBO = remoteLoadByUsernameService.loadByUsername(username);
            return CommonCloneUtil.objectClone(authUserBO, AuthUser.class);
        }
        return null;
    }

    @Override
    public Integer validateToken(String token, UserDetails userDetails, Device device) {
        return null;
    }
}
