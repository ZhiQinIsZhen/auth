package com.liyz.auth.common.base.desen.type;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeService;
import org.apache.commons.lang3.StringUtils;

/**
 * 注释:自定义脱敏
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/22 17:35
 */
public class DesensitizeSelfDefinition implements DesensitizeService {

    private static final String Card_No_Regex = "(\\d{4})\\d{10}(\\w{4})";

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.SELF_DEFINITION;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        if (StringUtils.isNotBlank(value)) {
            int length = value.length();
            int beginIndex = annotation.beginIndex();
            int endIndex = annotation.endIndex();
            if (beginIndex >= 0 && beginIndex < endIndex && endIndex <= length) {
                StringBuilder format = new StringBuilder(beginIndex > 0 ? "%s" : "");
                for (int i = endIndex - beginIndex; i > 0; i--) {
                    format.append("*");
                }
                format.append(endIndex == length ? "" : "%s");
                if (beginIndex > 0) {
                    value = String.format(format.toString(), value.substring(0, beginIndex), value.substring(endIndex));
                } else {
                    value = String.format(format.toString(), value.substring(endIndex));
                }
            }
        }
        return value;
    }
}
