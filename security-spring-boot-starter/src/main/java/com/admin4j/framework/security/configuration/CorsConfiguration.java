package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author andanyang
 * @Date 2021/6/8 10:42
 */
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfiguration {

    @Bean
    public CorsFilter corsFilter(CorsProperties properties) {

        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.addAllowedMethod(properties.getAllowedMethod());
        config.addAllowedOrigin(properties.getAllowedOrigin());
        config.addAllowedHeader(properties.getAllowedHeader());
        config.setMaxAge(properties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        CorsFilter corsFilter = new CorsFilter(source);

        return corsFilter;
    }
}
