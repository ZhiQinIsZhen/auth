package com.liyz.auth.common.base.desen;

/**
 * 注释:反序列化脱敏接口
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/6/2 16:49
 */
public interface DesensitizeService {

    /**
     * 获取脱敏类型
     *
     * @return
     */
    DesensitizationType getType();

    /**
     * 脱敏
     *
     * @param value
     * @param annotation
     * @return
     */
    String desensitize(String value, Desensitization annotation);
}
