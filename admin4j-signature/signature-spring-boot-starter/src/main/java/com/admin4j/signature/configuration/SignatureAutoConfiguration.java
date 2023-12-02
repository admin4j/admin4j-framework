package com.admin4j.signature.configuration;

import com.admin4j.framework.signature.core.DefaultSignatureStrategy;
import com.admin4j.framework.signature.core.SignatureApi;
import com.admin4j.framework.signature.core.filter.CacheRequestBodyFilter;
import com.admin4j.framework.signature.core.interceptor.SignatureInterceptor;
import com.admin4j.framework.signature.core.properties.SignatureProperties;
import com.admin4j.signature.SignatureGlobalExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhougang
 * @since 2023/11/10 15:10
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.web.servlet.HandlerInterceptor"})
@EnableConfigurationProperties(SignatureProperties.class)
@ConditionalOnProperty(prefix = "admin4j.signature", name = "enabled", matchIfMissing = true)
public class SignatureAutoConfiguration implements ApplicationContextAware {

    @Bean
    public CacheRequestBodyFilter cacheRequestBodyFilter() {
        return new CacheRequestBodyFilter();
    }

    /**
     * 配置CacheRequestBodyFilter对象过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<CacheRequestBodyFilter> filterRegistrationBean(CacheRequestBodyFilter cacheRequestBodyFilter) {
        FilterRegistrationBean<CacheRequestBodyFilter> filterRegistration = new FilterRegistrationBean<>(cacheRequestBodyFilter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(Integer.MAX_VALUE);
        return filterRegistration;
    }

    @Bean
    public SignatureInterceptor signatureInterceptor() {
        return new SignatureInterceptor();
    }

    @Bean
    public SignatureWebMvcConfigurer signatureWebMvcConfigurer(SignatureInterceptor signatureInterceptor) {
        return new SignatureWebMvcConfigurer(signatureInterceptor);
    }

    @Bean
    public DefaultSignatureStrategy defaultSignatureStrategy(StringRedisTemplate stringRedisTemplate,
                                                             SignatureProperties signatureProperties,
                                                             SignatureApi signatureApi) {
        return new DefaultSignatureStrategy(stringRedisTemplate, signatureProperties, signatureApi);
    }

    @Bean
    @Order(3)
    @ConditionalOnClass(name = "com.admin4j.common.pojo.SimpleResponse")
    public SignatureGlobalExceptionHandler signatureGlobalExceptionHandler() {
        return new SignatureGlobalExceptionHandler();
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
