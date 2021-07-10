package com.liyz.auth.common.base.desen.type;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeService;

/**
 * 注释:忽略此字符
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/10 20:00
 */
public class DesensitizeIgnore implements DesensitizeService {

    @Override
    public DesensitizationType getType() {
        return DesensitizationType.IGNORE;
    }

    @Override
    public String desensitize(String value, Desensitization annotation) {
        return null;
    }
}
