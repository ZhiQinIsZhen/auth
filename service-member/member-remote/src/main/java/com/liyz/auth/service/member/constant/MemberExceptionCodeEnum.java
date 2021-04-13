package com.liyz.auth.service.member.constant;

import com.liyz.auth.common.remote.exception.IExceptionCodeService;
import lombok.AllArgsConstructor;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 13:42
 */
@AllArgsConstructor
public enum MemberExceptionCodeEnum implements IExceptionCodeService {

    MobileExist("20001", "手机号已存在"),
    EmailExist("20002", "邮箱号已存在"),
    MobileEmailNonMatch("20003", "用户名格式不正确"),
    RegisterFail("20004", "注册失败"),
    AddressNonMatch("20005", "联系地址格式不正确"),
    SmsMobileSendAfter("20006", "短信已发送,请1分钟后重试"),
    SmsEmailSendAfter("20007", "邮件已发送,请1分钟后重试"),
    SmsLimit("20008", "信息发送已达上限"),
    UserNonExist("20009", "该用户不存在"),
            ;

    private String code;

    private String message;


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
