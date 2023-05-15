package com.admin4j.limiter.redis;

import com.admin4j.limiter.core.constant.LimiterType;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 固定窗口，限流
 *
 * @author andanyang
 * @since 2023/5/11 13:39
 */
public class LeakyBucketRedisRateLimiter extends AbstractRedisRateLimiter {


    public LeakyBucketRedisRateLimiter(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }

    @Override
    public LimiterType support() {
        return LimiterType.LEAKY_BUCKET;
    }

    @Override
    protected String getScriptName() {
        return "rateLimiter/LeakyBucket.lua";
    }
}
