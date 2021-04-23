package com.liyz.auth.security.client.advice;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 注释:异常统一处理
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2019/8/15 13:41
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class, Service.class})
public class AuthExceptionHandleAdvice {

    @ExceptionHandler({BadCredentialsException.class})
    public Result badCredentialsException(BadCredentialsException exception) {
        log.error(CommonExceptionCodeEnum.LoginFail.getMessage());
        return Result.error(CommonExceptionCodeEnum.LoginFail);
    }
}
