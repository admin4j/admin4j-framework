package com.admin4j.common.core.config;

import com.admin4j.common.core.exception.handler.GlobalExceptionHandler;
import com.admin4j.common.core.utils.SpringUtils;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2022/8/4 15:38
 */
public class SpringConfig {

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
