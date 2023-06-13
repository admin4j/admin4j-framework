package com.admin4j.framework.kaptcha.config;

import com.admin4j.framework.kaptcha.KaptchaFactory;
import com.admin4j.framework.kaptcha.propertie.KaptchaProperties;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 验证码配置
 */
@EnableConfigurationProperties(KaptchaProperties.class)
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha getKaptchaBeanMath(KaptchaProperties kaptchaProperties) {

        return KaptchaFactory.create(kaptchaProperties);
    }
}
