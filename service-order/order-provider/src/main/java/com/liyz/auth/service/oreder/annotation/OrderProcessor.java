package com.liyz.auth.service.oreder.annotation;

import com.liyz.auth.service.oreder.constant.OrderEventEnum;
import com.liyz.auth.service.oreder.constant.OrderStateEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 注释:状态机引擎的处理器注解标识
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:41
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface OrderProcessor {

    /**
     * 指定状态，state不能同时存在
     */
    OrderStateEnum[] state() default {};
    /**
     * 业务
     */
    String[] bizCode() default {};
    /**
     * 场景
     */
    String[] sceneId() default {};
    /**
     * 事件
     */
    OrderEventEnum event();
}
