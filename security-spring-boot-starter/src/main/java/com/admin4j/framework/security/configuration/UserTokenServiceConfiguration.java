package com.admin4j.framework.security.configuration;

import com.admin4j.common.constant.WebConstant;
import com.admin4j.framework.security.SecurityUserContextHolder;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.filter.ActuatorFilter;
import com.admin4j.framework.security.jwt.JwtUserDetailsService;
import com.admin4j.framework.security.jwt.JwtUserTokenService;
import com.admin4j.framework.security.properties.ActuatorProperties;
import com.admin4j.framework.security.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author andanyang
 * @since 2023/5/30 17:25
 */
@EnableConfigurationProperties(ActuatorProperties.class)
@AutoConfigureOrder(WebConstant.IUserContextHolderOrder + 2)
public class UserTokenServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean({UserTokenService.class})
    public UserTokenService userTokenService(JwtProperties jwtProperties, @Autowired(required = false) JwtUserDetailsService jwtUserDetailsService) {
        return new JwtUserTokenService(jwtProperties, jwtUserDetailsService);
    }

    /**
     * 设置密码编码器
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
    @ConditionalOnProperty(prefix = "admin4j.security.actuator", name = "enabled", matchIfMissing = false)
    public ActuatorFilter actuatorFilter() {
        return new ActuatorFilter();
    }


    @Bean
    public SecurityUserContextHolder securityUserContextHolder(@Autowired(required = false) JwtUserDetailsService jwtUserDetailsService) {
        return new SecurityUserContextHolder(jwtUserDetailsService);
    }
}
