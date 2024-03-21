package com.admin4j.framework.lock.configuration;

import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.RedissonLockExecutor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author andanyang
 * @since 2023/4/18 11:10
 */
@AutoConfigureOrder(800)
public class RedissonLockAutoConfiguration {

    @Bean
    @ConditionalOnClass(RedissonClient.class)
    @Primary
    @ConditionalOnMissingBean(RedissonLockExecutor.class)
    public LockExecutor redissonClient(RedissonClient redissonClient, ApplicationContext applicationContext) {
        RedissonLockExecutor redissonLockExecutor = new RedissonLockExecutor(redissonClient);
        if (applicationContext.containsBean("parentLockExecutor")) {
            redissonLockExecutor.setParent((LockExecutor<?>) applicationContext.getBean("parentLockExecutor"));
        }
        return redissonLockExecutor;
    }
}
