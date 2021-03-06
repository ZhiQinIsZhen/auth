package com.liyz.auth.common.controller.advice;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;

/**
 * 注释:异常统一处理
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2019/8/15 13:41
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice(annotations = {RestController.class, Controller.class, Service.class})
public class ControllerExceptionHandleAdvice {

    @ExceptionHandler({Exception.class})
    public Result exception(Exception exception) {
        log.error("未知异常", exception);
        return Result.error(CommonExceptionCodeEnum.UnknownException);
    }

    @ExceptionHandler({RpcException.class})
    public Result rpcException(RpcException exception) {
        log.error("远程服务调用异常->rpc", exception);
        return Result.error(CommonExceptionCodeEnum.RemoteServiceFail);
    }

    @ExceptionHandler({BindException.class})
    public Result bindException(BindException exception) {
        if (Objects.nonNull(exception) && exception.hasErrors()) {
            List<ObjectError> errors = exception.getAllErrors();
            for (ObjectError error : errors) {
                log.warn("参数校验 {} ：{}", error.getCodes()[0], error.getDefaultMessage());
                return Result.error(CommonExceptionCodeEnum.validated.getCode(), error.getDefaultMessage());
            }
        }
        log.error("参数校验出错了");
        return Result.error(CommonExceptionCodeEnum.validated);
    }

    @ExceptionHandler({ValidationException.class})
    public Result validationException(ValidationException exception) {
        String[] message = exception.getMessage().split(":");
        if (message.length >= 2) {
            log.warn("参数校验 exception ：{}", message);
            return Result.error(CommonExceptionCodeEnum.validated.getCode(), message[1].trim());
        }
        return Result.error(CommonExceptionCodeEnum.validated);
    }

    @ExceptionHandler({RemoteServiceException.class})
    public Result remoteServiceException(RemoteServiceException exception) {
        log.error("远程服务调用异常->remote  code:{}, message:{}", exception.getCode(), exception.getMessage());
        return Result.error(exception.getCode(), exception.getMessage());
    }

}
