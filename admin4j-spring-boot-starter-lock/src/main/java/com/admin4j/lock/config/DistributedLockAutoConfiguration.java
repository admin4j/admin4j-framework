package com.admin4j.lock.config;


import com.admin4j.lock.aspect.DistributedLockHandler;
import com.admin4j.lock.aspect.IdempotentHandler;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2021/12/23 8:41
 */
public class DistributedLockAutoConfiguration {

    @Bean
    public DistributedLockHandler distributedLockHandler() {
        return new DistributedLockHandler();
    }

    @Bean
    public IdempotentHandler idempotentHandler() {
        return new IdempotentHandler();
    }
}
