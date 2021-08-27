package com.liyz.auth.service.process.exception;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;
import com.liyz.auth.common.remote.exception.RemoteServiceException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:10
 */
public class RemoteProcessServiceException extends RemoteServiceException {

    public RemoteProcessServiceException() {
    }

    public RemoteProcessServiceException(String message) {
        super(message);
    }

    public RemoteProcessServiceException(String message, String code) {
        super(message, code);
    }

    public RemoteProcessServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteProcessServiceException(IExceptionCodeService codeService) {
        super(codeService.getMessage(), codeService.getCode());
    }
}
