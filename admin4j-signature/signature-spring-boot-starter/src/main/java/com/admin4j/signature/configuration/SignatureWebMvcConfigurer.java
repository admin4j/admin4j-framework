package com.admin4j.signature.configuration;

import com.admin4j.framework.signature.interceptor.SignatureInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhougang
 * @since 2023/11/10 15:12
 */
public class SignatureWebMvcConfigurer implements WebMvcConfigurer {

    private SignatureInterceptor signatureInterceptor;

    public SignatureWebMvcConfigurer(SignatureInterceptor signatureInterceptor) {
        this.signatureInterceptor = signatureInterceptor;
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
        registry.addInterceptor(signatureInterceptor).addPathPatterns("/**");
    }
}
