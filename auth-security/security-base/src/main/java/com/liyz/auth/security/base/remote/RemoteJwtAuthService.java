package com.liyz.auth.security.base.remote;

import com.liyz.auth.security.base.user.AuthUser;
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

    AuthUser login(final String username);

    AuthUser loadUserByUsername(final String username);

    AuthUser loadUserByToken(final String token);

    String getJWT(final ClaimDetail claimDetail);

    Integer validateToken(final String token, final UserDetails userDetails, final Integer device);
}
