package com.liyz.auth.common.base.trace.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.liyz.auth.common.base.filter.jackson.JacksonIgnoreContextValueFilter;

import java.lang.annotation.*;

/**
 * 注释:参数忽略注解
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/8 17:32
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE})
@JsonSerialize(using = JacksonIgnoreContextValueFilter.class)
@JacksonAnnotationsInside
public @interface LogIgnore {
}
