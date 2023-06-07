package com.admin4j.common.config;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.util.UserContextUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author andanyang
 * @since 2023/6/7 8:54
 */
public class UserContextConfig implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserContextUtil.userContextHolder = applicationContext.getBean(IUserContextHolder.class);
    }
}
