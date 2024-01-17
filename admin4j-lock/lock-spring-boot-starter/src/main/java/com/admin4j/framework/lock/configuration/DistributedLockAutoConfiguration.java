package com.admin4j.framework.lock.configuration;


import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.aspect.DistributedLockAspect;
import com.admin4j.framework.lock.aspect.IdempotentAspect;
import com.admin4j.framework.lock.util.DistributedLockUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2021/12/23 8:41
 */
@AutoConfigureOrder(900)
public class DistributedLockAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public DistributedLockAspect distributedLockHandler() {
        return new DistributedLockAspect();
    }

    @Bean
    public IdempotentAspect idempotentHandler() {
        return new IdempotentAspect();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        DistributedLockUtil.setDefaultLockExecutor(applicationContext.getBean(LockExecutor.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
