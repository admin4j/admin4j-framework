package com.admin4j.limiter.configuration;

import com.admin4j.limiter.core.interceptor.RateLimitInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author andanyang
 * @since 2023/5/11 15:12
 */
public class LimiterWebMvcConfigurer implements WebMvcConfigurer {


    private RateLimitInterceptor rateLimitInterceptor;

    public LimiterWebMvcConfigurer(RateLimitInterceptor rateLimitInterceptor) {
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
    }
}
