package com.admin4j.signature.configuration;

import com.admin4j.framework.signature.filter.RequestReplaceFilter;
import com.admin4j.framework.signature.interceptor.SignatureInterceptor;
import com.admin4j.framework.signature.properties.SignatureProperties;
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
    public RequestReplaceFilter requestReplaceFilter() {
        return new RequestReplaceFilter();
    }

    /**
     * 配置RequestReplaceFilter对象过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<RequestReplaceFilter> filterRegistrationBean(RequestReplaceFilter requestReplaceFilter) {
        FilterRegistrationBean<RequestReplaceFilter> filterRegistration = new FilterRegistrationBean<>(requestReplaceFilter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(2);
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
    @Order(2)
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
