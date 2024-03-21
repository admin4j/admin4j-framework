package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.properties.CorsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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
    public FilterRegistrationBean<CorsFilter> corsFilter(CorsProperties properties) {

        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.addAllowedMethod(properties.getAllowedMethod());
        config.addAllowedOrigin(properties.getAllowedOrigin());
        config.addAllowedHeader(properties.getAllowedHeader());
        config.setAllowCredentials(properties.isAllowCredentials());
        config.setMaxAge(properties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(properties.getUriPattern(), config);

        // 3. 返回新的CorsFilter
        CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(corsFilter);
        filterRegistrationBean.setOrder(-101);  // 小于 SpringSecurity Filter的 Order(-100)，UserFilter 即可
        return filterRegistrationBean;
    }
}
