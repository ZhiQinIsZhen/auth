package com.liyz.auth.common.base.desen.type;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeService;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:手机号码脱敏
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/22 17:35
 */
public class DesensitizeMobile implements DesensitizeService {

    private static final String Mobile_Regex = "(\\d{3})\\d{4}(\\d{4})";

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.MOBILE;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        if (StringUtils.isNotBlank(value)) {
            if (value.length() == 11) {
                value = value.replaceAll(Mobile_Regex, "$1****$2");
            } else {
                value = String.format("%s****%s", value.substring(0, 3), value.substring(7));
            }
        }
        return value;
    }
}
