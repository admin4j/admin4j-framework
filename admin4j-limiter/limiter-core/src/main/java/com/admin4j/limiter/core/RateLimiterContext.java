package com.admin4j.limiter.core;

import com.admin4j.limiter.core.constant.LimiterType;

/**
 * @author andanyang
 * @since 2023/5/11 14:53
 */
public interface RateLimiterContext {


    RateLimiterService getByLimiterType(LimiterType limiterType);
}
