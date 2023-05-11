package com.admin4j.limiter;

import com.admin4j.limiter.core.RateLimiterContext;
import com.admin4j.limiter.core.RateLimiterProvider;
import com.admin4j.limiter.core.RateLimiterService;
import com.admin4j.limiter.core.constant.LimiterType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/5/11 14:53
 */
public class DefaultRateLimiterContext implements ApplicationContextAware, InitializingBean, RateLimiterContext {

    private ApplicationContext applicationContext;

    private final Map<String, RateLimiterService> limiterServiceMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        applicationContext.getBeansOfType(RateLimiterProvider.class).values().forEach(i -> {

            limiterServiceMap.put(i.support().name(), i);
        });
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public RateLimiterService getByLimiterType(LimiterType limiterType) {
        return limiterServiceMap.get(limiterType.name());
    }
}
