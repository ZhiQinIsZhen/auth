package com.liyz.auth.common.base.desen.type;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeService;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:身份证脱敏
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/22 17:35
 */
public class DesensitizeCardNo implements DesensitizeService {

    private static final String Card_No_Regex = "(\\d{4})\\d{10}(\\w{4})";

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.CARD_NO;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        if (StringUtils.isNotBlank(value)) {
            if (value.length() == 18) {
                value = value.replaceAll(Card_No_Regex, "$1****$2");
            } else {
                value = String.format("%s****%s", value.substring(0, 4), value.substring(14));
            }
        }
        return value;
    }
}
