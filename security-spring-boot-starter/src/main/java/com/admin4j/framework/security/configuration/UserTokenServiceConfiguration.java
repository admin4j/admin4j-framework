package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.properties.JWTProperties;
import com.admin4j.framework.security.token.UserJWTTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author andanyang
 * @since 2023/5/30 17:25
 */
public class UserTokenServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean(UserTokenService.class)
    public UserTokenService userTokenService(JWTProperties jwtProperties) {
        return new UserJWTTokenService(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
