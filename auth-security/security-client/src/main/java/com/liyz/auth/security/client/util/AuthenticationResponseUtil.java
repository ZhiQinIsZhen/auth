package com.liyz.auth.security.client.util;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.util.JsonMapperUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注释:认证返回工具类
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/8/18 11:36
 */
public class AuthenticationResponseUtil {

    public static void authFail(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JsonMapperUtil.toJSONString(Result.error(CommonExceptionCodeEnum.AuthorizationFail)));
        httpServletResponse.addHeader("Session-Invalid","true");
    }

    public static void authForbidden(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JsonMapperUtil.toJSONString(Result.error(CommonExceptionCodeEnum.FORBIDDEN)));
        httpServletResponse.addHeader("Session-Invalid","true");
    }

    public static void authExpired(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JsonMapperUtil.toJSONString(Result.error(CommonExceptionCodeEnum.AuthorizationTimeOut)));
        httpServletResponse.addHeader("Session-Invalid","true");
    }

    public static void authOthersLogin(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JsonMapperUtil.toJSONString(Result.error(CommonExceptionCodeEnum.OthersLogin)));
        httpServletResponse.addHeader("Session-Invalid","true");
    }
}
