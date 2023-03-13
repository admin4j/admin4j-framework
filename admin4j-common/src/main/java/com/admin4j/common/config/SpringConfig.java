package com.admin4j.common.config;

import com.admin4j.common.util.SpringUtils;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/3/13 14:14
 */
public class SpringConfig {
    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
}
