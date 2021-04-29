package com.liyz.auth.common.limit.exception;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;
import com.liyz.auth.common.remote.exception.RemoteServiceException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 16:31
 */
public class LimitException extends RemoteServiceException {
    private static final long serialVersionUID = 5238675226570828607L;

    public LimitException() {
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(String message, String code) {
        super(message, code);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(IExceptionCodeService codeService) {
        super(codeService);
    }
}
