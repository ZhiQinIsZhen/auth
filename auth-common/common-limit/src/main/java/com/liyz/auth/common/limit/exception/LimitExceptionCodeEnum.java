package com.liyz.auth.common.limit.exception;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 15:09
 */
public enum LimitExceptionCodeEnum implements IExceptionCodeService {


    OutLimitCount("30001", "超出最大访问限制"),
    LimitRequest("30002", "限制访问"),
    NoSupportLimitType("30003", "不支持当前限流类型"),
    ;

    private String code;

    private String message;

    LimitExceptionCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
