package com.admin4j.framework.alert.autoconfigure;

import com.admin4j.framework.alert.listener.GlobalExceptionListener;
import com.admin4j.framework.alert.props.AlertProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2024/2/22 11:22
 */
@EnableConfigurationProperties(AlertProperties.class)
public class AlertAutoconfigure {

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionListener.class)
    @ConditionalOnProperty(prefix = "admin4j.alert.enable")
    GlobalExceptionListener globalExceptionListener() {
        return new GlobalExceptionListener();
    }
}
