package com.admin4j.limiter.core.anno;

import com.admin4j.limiter.core.constant.LimiterType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/5/11 9:45
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RateLimiter {

    String key();

    /**
     * 限速类型
     */
    LimiterType limiterType() default LimiterType.SLIDING_WINDOW;

    /**
     * qps
     */
    int qps() default 2;

    /**
     * 统计时间范围
     */
    int time() default 2;

    /**
     * 统计时间范围,单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;


}