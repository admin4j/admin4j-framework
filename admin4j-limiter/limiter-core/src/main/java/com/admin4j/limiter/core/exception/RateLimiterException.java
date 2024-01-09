package com.admin4j.limiter.core.exception;

/**
 * @author andanyang
 * @since 2023/5/11 17:05
 */
public class RateLimiterException extends RuntimeException {

    public RateLimiterException() {

        super("Too Many Request");
    }

    public RateLimiterException(String key) {

        super("Too Many Request:" + key);
    }
}
