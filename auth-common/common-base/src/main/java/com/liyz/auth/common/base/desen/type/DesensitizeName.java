package com.liyz.auth.common.base.desen.type;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeService;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:姓名脱敏
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/22 17:35
 */
public class DesensitizeName implements DesensitizeService {

    private static final String Name_Regex = "(\\W)(\\W+)";

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.REAL_NAME;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        if (StringUtils.isNotBlank(value) && value.length() > 1) {
            value = value.replaceAll(Name_Regex, "$1*");
        }
        return value;
    }
}
