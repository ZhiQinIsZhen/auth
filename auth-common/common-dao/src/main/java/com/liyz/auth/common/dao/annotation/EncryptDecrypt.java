package com.liyz.auth.common.dao.annotation;

import java.lang.annotation.*;

/**
 * 注释:加解密
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/24 14:58
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptDecrypt {
}
