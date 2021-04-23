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
public class DesensitizeDefault implements DesensitizeService {

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.CARD_NO;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        return value;
    }
}
