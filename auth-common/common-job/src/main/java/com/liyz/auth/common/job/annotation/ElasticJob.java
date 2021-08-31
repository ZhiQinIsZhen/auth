package com.liyz.auth.common.job.annotation;

import java.lang.annotation.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/31 14:29
 */
@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJob {

    String cron();

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    boolean overwrite() default true;
}
