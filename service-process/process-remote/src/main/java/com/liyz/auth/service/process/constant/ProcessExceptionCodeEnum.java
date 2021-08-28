package com.liyz.auth.service.process.constant;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:25
 */
public enum ProcessExceptionCodeEnum implements IExceptionCodeService {
    FILE_PATTERN_ERROR("70001", "流程文件格式不对"),
    TASK_HAS_SUBMIT("70002", "任务已被提交"),
    TASK_SUBMIT_NON_RIGHT("70003", "该任务无权提交"),
    ;

    private String code;

    private String message;

    ProcessExceptionCodeEnum(String code, String message) {
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
