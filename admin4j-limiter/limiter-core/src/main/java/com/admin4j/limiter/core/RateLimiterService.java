package com.admin4j.limiter.core;

/**
 * @author andanyang
 * @since 2023/5/11 9:42
 */
public interface RateLimiterService {


    /**
     * 判断请求是否允许通过
     *
     * @return
     */
    boolean tryAcquire(String key, int qps, int time);
}
