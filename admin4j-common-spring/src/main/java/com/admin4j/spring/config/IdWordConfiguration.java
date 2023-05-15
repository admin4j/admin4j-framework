package com.admin4j.spring.config;


import com.admin4j.common.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * @author andanyang
 * @since 2022/8/4 15:23
 */
public class IdWordConfiguration {
    @Bean
    @Lazy
    public SnowflakeIdWorker idWorker() {
        SnowflakeIdWorker snowflakeIdWorkerUtil = new SnowflakeIdWorker();
        return snowflakeIdWorkerUtil;
    }
}
