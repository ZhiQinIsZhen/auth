package com.liyz.auth.service.member.exception;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;
import com.liyz.auth.common.remote.exception.RemoteServiceException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 10:59
 */
public class RemoteMemberServiceException extends RemoteServiceException {

    public RemoteMemberServiceException() {
    }

    public RemoteMemberServiceException(String message) {
        super(message);
    }

    public RemoteMemberServiceException(String message, String code) {
        super(message, code);
    }

    public RemoteMemberServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteMemberServiceException(IExceptionCodeService codeService) {
        super(codeService.getMessage(), codeService.getCode());
    }
}
