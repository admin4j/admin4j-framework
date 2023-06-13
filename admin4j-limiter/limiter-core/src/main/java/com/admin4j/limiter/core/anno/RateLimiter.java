package com.admin4j.limiter.core.anno;

import com.admin4j.limiter.core.RateLimiterKeyGenerate;
import com.admin4j.limiter.core.constant.LimiterType;
import com.admin4j.limiter.core.key.DefaultRateLimiterKeyGenerate;

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

    Class<? extends RateLimiterKeyGenerate> keyGenerate() default DefaultRateLimiterKeyGenerate.class;

    /**
     * 限速类型
     */
    LimiterType limiterType() default LimiterType.SLIDING_WINDOW;

    /**
     * qps，容量，最大的请求次数
     */
    int maxAttempts() default 2;

    /**
     * 统计时间范围
     */
    int interval() default 2;

    /**
     * 统计时间范围,单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否拼上ip
     *
     * @return
     */
    boolean ip() default false;
}