package com.admin4j.framework.signature.core.interceptor;

import com.admin4j.framework.signature.core.SignatureStrategy;
import com.admin4j.framework.signature.core.annotation.Signature;
import com.admin4j.framework.signature.core.exception.SignatureException;
import com.admin4j.spring.util.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author zhougang
 * @since 2023/11/10 9:51
 */
public class SignatureInterceptor implements HandlerInterceptor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public SignatureInterceptor() {

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

        Signature signature = handlerMethod.getMethodAnnotation(Signature.class);

        if (signature == null) {
            Class<?> beanType = handlerMethod.getBeanType();
            Signature annotation = beanType.getAnnotation(Signature.class);
            if (annotation == null) {
                return true;
            }
            signature = annotation;
        }

        if (!signature.enable()) {
            return true;
        }

        SignatureStrategy signatureStrategy = SpringUtils.getBean(signature.signatureClass());
        if (!signatureStrategy.verify(signature, request)) {
            throw new SignatureException("Signature failure");
        }
        return true;
    }
}
