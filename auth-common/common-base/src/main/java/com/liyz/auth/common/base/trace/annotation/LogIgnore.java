package com.liyz.auth.common.base.trace.annotation;

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
@Target({ElementType.PARAMETER})
public @interface LogIgnore {
}
