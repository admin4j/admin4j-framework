package com.admin4j.limiter.core.util;

import com.admin4j.limiter.core.RateLimiterContext;
import com.admin4j.limiter.core.RateLimiterService;
import com.admin4j.limiter.core.constant.LimiterType;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * 令牌桶算法限速工具类
 *
 * @author andanyang
 * @since 2023/5/15 9:51
 */
public class RateLimiterUtil {

    @Setter
    private static RateLimiterContext rateLimiterContext;

    /**
     * 限速工具类方法
     *
     * @param key      业务唯一key
     * @param capacity 最大容量
     * @param interval 时间窗口，单位 秒。 interval 秒 可以处理，capacity个请求
     * @return
     */
    public static boolean rateLimiter(LimiterType limiterType, String key, int capacity, int interval) {
        RateLimiterService limiterService = rateLimiterContext.getByLimiterType(limiterType);
        return limiterService.tryAcquire(key, capacity, interval);
    }

    public static void rateLimiter(LimiterType limiterType, String key, int maxAttempts, int interval, Runnable runnable) {
        RateLimiterService limiterService = rateLimiterContext.getByLimiterType(limiterType);
        if (limiterService.tryAcquire(key, maxAttempts, interval)) {
            runnable.run();
        }
    }

    public static <T> T rateLimiter(LimiterType limiterType, String key, int maxAttempts, int interval, Supplier<T> supplier) {
        RateLimiterService limiterService = rateLimiterContext.getByLimiterType(limiterType);
        if (limiterService.tryAcquire(key, maxAttempts, interval)) {
            return supplier.get();
        }

        return null;
    }
}
