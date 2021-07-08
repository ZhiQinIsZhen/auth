package com.liyz.auth.common.base.trace.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注释:log aspect注解
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/8 17:32
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Logs {

    @AliasFor("method")
    String value() default "";

    @AliasFor("value")
    String method() default "";

    boolean before() default true;

    boolean after() default true;

    boolean exception() default true;
}
