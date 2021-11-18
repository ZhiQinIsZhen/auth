package com.liyz.auth.security.base.remote;

import com.liyz.auth.common.base.util.AuthUser;
import com.liyz.auth.security.base.constant.SecurityEnum;
import com.liyz.auth.security.base.user.ClaimDetail;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 17:07
 */
public interface RemoteJwtAuthService {

    /**
     * 登陆
     *
     * @param username
     * @param audienceType
     * @return
     */
    AuthUser login(final String username, final SecurityEnum.AudienceType audienceType);

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @param audienceType
     * @return
     */
    AuthUser loadUserByUsername(final String username, final SecurityEnum.AudienceType audienceType);

    AuthUser loadUserByToken(final String token);

    String getJWT(final ClaimDetail claimDetail);

    Integer validateToken(final String token, final UserDetails userDetails, final Integer device);
}
