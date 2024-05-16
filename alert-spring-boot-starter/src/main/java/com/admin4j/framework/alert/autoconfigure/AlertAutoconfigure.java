package com.admin4j.framework.alert.autoconfigure;

import com.admin4j.framework.alert.aspect.ExceptionHandlerAspect;
import com.admin4j.framework.alert.listener.GlobalExceptionListener;
import com.admin4j.framework.alert.listener.StartupListener;
import com.admin4j.framework.alert.props.AlertProperties;
import com.admin4j.framework.alert.send.DingTalkAbstractSendAlertMessageService;
import com.admin4j.framework.alert.send.QyWeixinAbstractSendAlertMessageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author andanyang
 * @since 2024/2/22 11:22
 */
@EnableConfigurationProperties(AlertProperties.class)
@ConditionalOnProperty(prefix = "admin4j.alert", name = "enable")
public class AlertAutoconfigure {

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionListener.class)
    public GlobalExceptionListener globalExceptionListener() {
        return new GlobalExceptionListener();
    }

    @Bean
    public ExceptionHandlerAspect exceptionHandlerAspect() {
        return new ExceptionHandlerAspect();
    }

    @Bean
    @ConditionalOnMissingBean(DingTalkAbstractSendAlertMessageService.class)
    @ConditionalOnProperty(prefix = "admin4j.alert", name = "ding-talk-webhook-url")
    public DingTalkAbstractSendAlertMessageService dingTalkAbstractSendAlertMessageService(AlertProperties alertProperties) {
        return new DingTalkAbstractSendAlertMessageService(alertProperties);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(QyWeixinAbstractSendAlertMessageService.class)
    @ConditionalOnProperty(prefix = "admin4j.alert", name = "qy-wei-xin-webhook-url")
    public QyWeixinAbstractSendAlertMessageService qyWeixinAbstractSendAlertMessageService(AlertProperties alertProperties) {
        return new QyWeixinAbstractSendAlertMessageService(alertProperties);
    }

    @Bean
    public StartupListener startupListener() {
        return new StartupListener();
    }
}
