package com.liyz.auth.security.remote;

import com.liyz.auth.security.remote.bo.AuthUserBO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 14:49
 */
public interface RemoteLoadByUsernameService {

    AuthUserBO loadByUsername(String username);
}
