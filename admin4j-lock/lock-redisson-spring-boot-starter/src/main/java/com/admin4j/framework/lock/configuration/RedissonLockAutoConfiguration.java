package com.admin4j.framework.lock.configuration;

import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.RedissonLockExecutor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

/**
 * @author andanyang
 * @since 2023/4/18 11:10
 */
public class RedissonLockAutoConfiguration {

    @Bean
    @Order(800)
    //@ConditionalOnBean(RedissonClient.class)
    @DependsOn("redisson")
    @ConditionalOnClass(RedissonClient.class)
    @Primary
    public LockExecutor redissonClient(RedissonClient redissonClient) {
        return new RedissonLockExecutor(redissonClient);
    }
}
