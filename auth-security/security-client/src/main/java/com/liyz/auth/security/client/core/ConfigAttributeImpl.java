package com.liyz.auth.security.client.core;

import lombok.Getter;
import org.springframework.security.access.ConfigAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 9:35
 */
public class ConfigAttributeImpl implements ConfigAttribute {

    @Getter
    private final HttpServletRequest httpServletRequest;

    public ConfigAttributeImpl(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }


    @Override
    public String getAttribute() {
        return null;
    }
}
