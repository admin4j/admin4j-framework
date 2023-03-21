package com.admin4j.framework.prometheus.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/3/21 10:08
 */
public class PrometheusAutoConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> appMetricsCommonTags() {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
