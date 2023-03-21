package com.admin4j.framework.web.config;

import com.admin4j.framework.web.utils.SnowflakeIdWorker;
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
