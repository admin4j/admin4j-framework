package com.admin4j.limiter.core.interceptor;

import com.admin4j.limiter.core.RateLimiterContext;
import com.admin4j.limiter.core.RateLimiterService;
import com.admin4j.limiter.core.anno.RateLimiter;
import com.admin4j.limiter.core.exception.RateLimiterException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author andanyang
 * @since 2023/5/11 9:51
 */
public class RateLimitInterceptor implements HandlerInterceptor {


    private RateLimiterContext rateLimiterContext;

    public RateLimitInterceptor(RateLimiterContext rateLimiterContext) {
        this.rateLimiterContext = rateLimiterContext;
    }

    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        RateLimiter rateLimiter = handlerMethod.getMethodAnnotation(RateLimiter.class);
        //handlerMethod.getBean()
        if (rateLimiter == null) {
            return true;
        }

        RateLimiterService rateLimiterService = rateLimiterService(rateLimiter);
//TODO KEY generate
        if (!rateLimiterService.tryAcquire(rateLimiter.key(), rateLimiter.qps(), rateLimiter.time())) {

            throw new RateLimiterException();
        }
        return true;
    }


    protected RateLimiterService rateLimiterService(RateLimiter rateLimiter) {
        return rateLimiterContext.getByLimiterType(rateLimiter.limiterType());
    }
}
