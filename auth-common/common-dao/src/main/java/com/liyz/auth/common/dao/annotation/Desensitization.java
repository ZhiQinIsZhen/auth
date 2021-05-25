package com.liyz.auth.common.dao.annotation;

import java.lang.annotation.*;

/**
 * 注释:脱敏
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/24 14:56
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitization {
}
