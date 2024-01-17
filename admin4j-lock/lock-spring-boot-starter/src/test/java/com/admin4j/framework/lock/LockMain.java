package com.admin4j.framework.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2024/1/17 15:28
 */
@SpringBootTest
public class LockMain {
    public static void main(String[] args) {
        SpringApplication.run(LockSpringTest.class, args);
    }


    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.0.252:6379");
        return Redisson.create(config);
    }
}
