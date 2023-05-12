package com.admin4j.limiter.core;

/**
 * @author andanyang
 * @since 2023/5/11 9:42
 */
public interface RateLimiterService {

    /**
     * 判断请求是否允许通过
     *
     * @param maxAttempts qps、最大的容量
     * @param interval    统计时间间隔
     * @return 是否限速
     */
    boolean tryAcquire(String key, int maxAttempts, long interval);
}
