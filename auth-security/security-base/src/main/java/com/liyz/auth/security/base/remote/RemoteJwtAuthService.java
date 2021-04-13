package com.liyz.auth.security.base.remote;

import com.liyz.auth.security.base.user.AuthUser;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 17:07
 */
public interface RemoteJwtAuthService {

    AuthUser loadUserByToken(final String token);

    Integer validateToken(final String token, final UserDetails userDetails, final Device device);
}
