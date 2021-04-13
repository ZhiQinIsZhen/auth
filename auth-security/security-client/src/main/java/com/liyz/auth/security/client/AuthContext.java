package com.liyz.auth.security.client;

import com.liyz.auth.security.base.user.AuthUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 注释:认证用户上下文
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 16:06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthContext {

    /**
     * 用户信息
     */
    private static ThreadLocal<AuthUser> authUserContainer = new ThreadLocal<>();

    /**
     * 获取认证用户
     *
     * @return
     */
    public static AuthUser getAuthUser() {
        return authUserContainer.get();
    }

    /**
     * 添加认证用户
     *
     * @param authUser
     */
    public static void setAuthUser(AuthUser authUser) {
        authUserContainer.set(authUser);
    }

    /**
     * 移除认证用户
     */
    public static void remove() {
        authUserContainer.remove();
    }
}
