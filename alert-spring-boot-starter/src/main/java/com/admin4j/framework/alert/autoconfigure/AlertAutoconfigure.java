package com.admin4j.framework.alert.autoconfigure;

import com.admin4j.framework.alert.listener.GlobalExceptionListener;
import com.admin4j.framework.alert.props.AlertProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2024/2/22 11:22
 */
@EnableConfigurationProperties(AlertProperties.class)
public class AlertAutoconfigure {

    @Bean
    GlobalExceptionListener globalExceptionListener() {
        return new GlobalExceptionListener();
    }
}
