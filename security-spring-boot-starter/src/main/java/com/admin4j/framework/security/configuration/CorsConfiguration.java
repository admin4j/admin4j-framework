package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.properties.CorsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author andanyang
 * @Date 2021/6/8 10:42
 */
@EnableConfigurationProperties({CorsProperties.class})
public class CorsConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "admin4j.security.cors", name = "enabled", matchIfMissing = true)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(CorsProperties properties) {

        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.addAllowedMethod(properties.getAllowedMethod());
        config.addAllowedOrigin(properties.getAllowedOrigin());
        config.addAllowedHeader(properties.getAllowedHeader());
        config.setMaxAge(properties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
