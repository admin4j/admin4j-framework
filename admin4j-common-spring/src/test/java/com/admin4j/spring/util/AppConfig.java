package com.admin4j.spring.util;

import com.admin4j.spring.config.SpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author andanyang
 * @since 2023/5/23 14:30
 */
@Configuration
@ComponentScan("com.admin4j.spring")
@Import(SpringConfig.class)
public class AppConfig {
}
