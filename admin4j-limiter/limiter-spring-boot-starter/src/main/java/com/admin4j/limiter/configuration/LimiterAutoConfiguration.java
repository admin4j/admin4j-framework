package com.admin4j.limiter.configuration;

import com.admin4j.common.service.ILoginTenantInfoService;
import com.admin4j.common.service.ILoginUserInfoService;
import com.admin4j.limiter.DefaultRateLimiterContext;
import com.admin4j.limiter.LimiterGlobalExceptionHandler;
import com.admin4j.limiter.core.RateLimiterContext;
import com.admin4j.limiter.core.RateLimiterKeyGenerate;
import com.admin4j.limiter.core.interceptor.RateLimitInterceptor;
import com.admin4j.limiter.core.key.DefaultRateLimiterKeyGenerate;
import com.admin4j.limiter.key.TenantRateLimiterKeyGenerate;
import com.admin4j.limiter.key.UserRateLimiterKeyGenerate;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

/**
 * @author andanyang
 * @since 2023/5/11 15:10
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.web.servlet.HandlerInterceptor"})
public class LimiterAutoConfiguration implements ApplicationContextAware {

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

    @Bean
    @Order(2)
    @ConditionalOnClass(name = "com.admin4j.common.pojo.SimpleResponse")
    public LimiterGlobalExceptionHandler alimiterGlobalExceptionHandler() {
        return new LimiterGlobalExceptionHandler();
    }

    @Bean
    @Primary
    public RateLimiterKeyGenerate rateLimiterKeyGenerate() {
        return new DefaultRateLimiterKeyGenerate();
    }

    @Bean
    @ConditionalOnBean(ILoginUserInfoService.class)
    public RateLimiterKeyGenerate userRateLimiterKeyGenerate(ILoginUserInfoService loginUserInfoService) {
        return new UserRateLimiterKeyGenerate(loginUserInfoService);
    }

    @Bean
    @ConditionalOnBean(ILoginTenantInfoService.class)
    public RateLimiterKeyGenerate tenantRateLimiterKeyGenerate(ILoginTenantInfoService tenantInfoService) {
        return new TenantRateLimiterKeyGenerate(tenantInfoService);
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //@PostConstruct
    //public void afterPropertiesSet() throws Exception {
    //    RateLimiterContext bean = applicationContext.getBean(RateLimiterContext.class);
    //    RateLimiterUtil.setRateLimiterContext(bean);
    //}
}
