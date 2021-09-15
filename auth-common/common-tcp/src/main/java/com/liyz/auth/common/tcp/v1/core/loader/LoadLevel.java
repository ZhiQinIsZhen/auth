package com.liyz.auth.common.tcp.v1.core.loader;

import java.lang.annotation.*;

/**
 * 注释:The interface Load level.
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 16:05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LoadLevel {

    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Order int.
     *
     * @return the int
     */
    int order() default 0;

    /**
     * Scope enum.
     * @return
     */
    Scope scope() default Scope.SINGLETON;
}
