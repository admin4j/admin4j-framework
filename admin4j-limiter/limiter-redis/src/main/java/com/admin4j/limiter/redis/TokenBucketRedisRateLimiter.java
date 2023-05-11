package com.admin4j.limiter.redis;

import com.admin4j.limiter.core.constant.LimiterType;

/**
 * 固定窗口，限流
 *
 * @author andanyang
 * @since 2023/5/11 13:39
 */
public class TokenBucketRedisRateLimiter extends AbstractRedisRateLimiter {

    //public TokenBucketRedisRateLimiter(StringRedisTemplate stringRedisTemplate) {
    //    super(stringRedisTemplate);
    //}

    @Override
    public LimiterType support() {
        return LimiterType.TOKEN_BUCKET;
    }

    @Override
    protected String getScriptName() {
        return "rateLimiter/TokenBucket.lua";
    }
}
