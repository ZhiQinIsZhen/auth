package com.liyz.auth.common.remote.exception;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 15:09
 */
public enum CommonExceptionCodeEnum implements IExceptionCodeService {

    success("0", "成功"),
    FORBIDDEN("401", "登陆后进行操作"),
    NO_RIGHT("401", "暂无权限"),
    validated("10000", "参数校验失败"),
    UnknownException("10001", "未知异常"),
    AuthorizationFail("10002", "认证失败"),
    AuthorizationTimeOut("10003", "认证超时"),
    LoginFail("10004", "用户名或者密码错误"),
    RemoteServiceFail("10005", "服务异常"),
    NoData("10006", "暂无数据"),
    ParameterError("10008", "参数异常"),
    LimitCount("1009", "超出最大访问限制"),
    ImageCodeError("10010", "图片验证码不正确"),
    MobileCodeError("10011", "短信验证码不正确"),
    EmailCodeError("10012", "邮件验证码不正确"),
    OldFileNotExist("10013", "原文件不存在"),
    OthersLogin("10014", "该账户已在其他地方登陆"),
    ;

    private String code;

    private String message;

    CommonExceptionCodeEnum(String code, String message) {
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
