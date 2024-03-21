package com.admin4j.framework.lock.configuration;

import com.admin4j.framework.lock.LocalLockExecutor;
import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.pool.WrapperLockObject;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/4/18 11:10
 */
@AutoConfigureOrder(700)
public class LocalLockAutoConfiguration {

    @Bean("parentLockExecutor")
    @ConditionalOnMissingBean(value = LocalLockExecutor.class, name = "parentLockExecutor")
    public LockExecutor<WrapperLockObject> localLockExecutor() {
        return new LocalLockExecutor();
    }
}
