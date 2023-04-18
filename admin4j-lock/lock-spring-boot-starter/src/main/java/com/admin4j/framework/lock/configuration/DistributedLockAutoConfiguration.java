package com.admin4j.framework.lock.configuration;


import com.admin4j.common.util.SpringUtils;
import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.aspect.DistributedLockAspect;
import com.admin4j.framework.lock.aspect.IdempotentAspect;
import com.admin4j.framework.lock.util.DistributedLockUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2021/12/23 8:41
 */
public class DistributedLockAutoConfiguration implements InitializingBean {

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

        DistributedLockUtil.setDefaultLockExecutor(SpringUtils.getBean(LockExecutor.class));
    }
}
