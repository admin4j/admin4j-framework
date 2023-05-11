package com.admin4j.limiter.configuration;

import com.admin4j.limiter.DefaultRateLimiterContext;
import com.admin4j.limiter.core.RateLimiterContext;
import com.admin4j.limiter.core.interceptor.RateLimitInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author andanyang
 * @since 2023/5/11 15:10
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.web.servlet.HandlerInterceptor"})
public class LimiterAutoConfiguration {

    @Bean
    public RateLimitInterceptor rateLimitInterceptor(RateLimiterContext rateLimiterContext) {
        return new RateLimitInterceptor(rateLimiterContext);
    }

    @Bean
    public RateLimiterContext rateLimiterContext() {
        return new DefaultRateLimiterContext();
    }

    @Bean
    public LimiterWebMvcConfigurer limiterWebMvcConfigurer(RateLimitInterceptor rateLimitInterceptor) {
        return new LimiterWebMvcConfigurer(rateLimitInterceptor);
    }
}
