package com.admin4j.common.config;

import com.admin4j.common.constant.WebConstant;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.service.impl.SimpleUserContextHolder;
import com.admin4j.common.service.impl.TtlUserContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author andanyang
 * @since 2023/9/15 9:19
 */
@Configuration
@AutoConfigureOrder(WebConstant.IUserContextHolderOrder + 6)
public class UserContextAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    @Lazy
    @ConditionalOnMissingBean(IUserContextHolder.class)
    @ConditionalOnMissingClass("com.alibaba.ttl.TransmittableThreadLocal")
    public IUserContextHolder userContextHolder() {
        return new SimpleUserContextHolder();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean(IUserContextHolder.class)
    @ConditionalOnClass(name = "com.alibaba.ttl.TransmittableThreadLocal")
    public IUserContextHolder ttlUserContextHolder() {
        return new TtlUserContextHolder();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
