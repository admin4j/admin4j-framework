package com.admin4j.limiter.core;

import com.admin4j.limiter.core.constant.LimiterType;

/**
 * @author andanyang
 * @since 2023/5/11 10:16
 */
public interface RateLimiterProvider extends RateLimiterService {

    LimiterType support();
}
