package com.admin4j.spring.config;

import com.admin4j.spring.util.SpringUtils;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/3/13 14:14
 */
public class SpringConfig {
    @Bean("admin4jSpringUtils")
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
}
