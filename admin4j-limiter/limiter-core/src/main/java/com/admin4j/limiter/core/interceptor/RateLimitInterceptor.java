package com.admin4j.limiter.core.interceptor;

import com.admin4j.limiter.core.RateLimiterContext;
import com.admin4j.limiter.core.RateLimiterKeyGenerate;
import com.admin4j.limiter.core.RateLimiterService;
import com.admin4j.limiter.core.anno.RateLimiter;
import com.admin4j.limiter.core.exception.RateLimiterException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/5/11 9:51
 */
public class RateLimitInterceptor implements HandlerInterceptor, ApplicationContextAware {


    private RateLimiterContext rateLimiterContext;
    private ApplicationContext applicationContext;

    public RateLimitInterceptor(RateLimiterContext rateLimiterContext) {
        this.rateLimiterContext = rateLimiterContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        RateLimiter rateLimiter = handlerMethod.getMethodAnnotation(RateLimiter.class);

        if (rateLimiter == null) {
            Class<?> beanType = handlerMethod.getBeanType();
            RateLimiter annotation = beanType.getAnnotation(RateLimiter.class);
            if (annotation == null) {
                return true;
            }
            rateLimiter = annotation;
        }

        String key = null;
        RateLimiterKeyGenerate rateLimiterKeyGenerate = applicationContext.getBean(rateLimiter.keyGenerate());
        Assert.notNull(rateLimiterKeyGenerate, "keyGenerate not null");
        key = rateLimiterKeyGenerate.generateKey(request, response, handlerMethod);


        RateLimiterService rateLimiterService = rateLimiterService(rateLimiter);
        long interval = rateLimiter.interval();
        if (!rateLimiter.timeUnit().equals(TimeUnit.SECONDS)) {
            interval = rateLimiter.timeUnit().toSeconds(interval);
        }
        if (!rateLimiterService.tryAcquire(key, rateLimiter.maxAttempts(), interval)) {

            throw new RateLimiterException();
        }
        return true;
    }


    protected RateLimiterService rateLimiterService(RateLimiter rateLimiter) {
        return rateLimiterContext.getByLimiterType(rateLimiter.limiterType());
    }
}
