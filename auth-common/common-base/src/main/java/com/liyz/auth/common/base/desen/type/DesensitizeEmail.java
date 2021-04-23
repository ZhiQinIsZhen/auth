package com.liyz.auth.common.base.desen.type;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeService;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:邮箱脱敏
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/22 17:35
 */
public class DesensitizeEmail implements DesensitizeService {

    private static final String Email_Regex = "(\\w+)\\w{5}@(\\w+)";

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.EMAIL;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replaceAll(Email_Regex, "$1****$2");
        }
        return value;
    }
}
