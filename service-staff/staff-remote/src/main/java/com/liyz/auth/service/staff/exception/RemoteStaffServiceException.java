package com.liyz.auth.service.staff.exception;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;
import com.liyz.auth.common.remote.exception.RemoteServiceException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 10:59
 */
public class RemoteStaffServiceException extends RemoteServiceException {

    public RemoteStaffServiceException() {
    }

    public RemoteStaffServiceException(String message) {
        super(message);
    }

    public RemoteStaffServiceException(String message, String code) {
        super(message, code);
    }

    public RemoteStaffServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteStaffServiceException(IExceptionCodeService codeService) {
        super(codeService.getMessage(), codeService.getCode());
    }
}
