package com.admin4j.limiter.redis;

import com.admin4j.limiter.core.constant.LimiterType;
import org.springframework.context.annotation.Configuration;

/**
 * 固定窗口，限流
 *
 * @author andanyang
 * @since 2023/5/11 13:39
 */
@Configuration
public class FixWindowRedisRateLimiter extends AbstractRedisRateLimiter {


    //public FixWindowRedisRateLimiter(StringRedisTemplate stringRedisTemplate) {
    //    super(stringRedisTemplate);
    //}

    @Override
    public LimiterType support() {
        return LimiterType.FIX_WINDOW;
    }

    @Override
    protected String getScriptName() {
        return "rateLimiter/FixWindow.lua";
    }
}
