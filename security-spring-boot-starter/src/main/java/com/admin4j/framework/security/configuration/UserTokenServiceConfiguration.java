package com.admin4j.framework.security.configuration;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.context.SecurityUserContextHolder;
import com.admin4j.framework.security.jwt.JwtUserDetailsService;
import com.admin4j.framework.security.jwt.JwtUserTokenService;
import com.admin4j.framework.security.mult.UsernamePasswordUserDetailsService;
import com.admin4j.framework.security.properties.FormLoginProperties;
import com.admin4j.framework.security.properties.JwtProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author andanyang
 * @since 2023/5/30 17:25
 */
public class UserTokenServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean(UserTokenService.class)
    public UserTokenService userTokenService(JwtProperties jwtProperties, JwtUserDetailsService jwtUserDetailsService) {
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

    /**
     * 默认的表达登录
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(UsernamePasswordUserDetailsService.class)
    @ConditionalOnBean(UserDetailsService.class)
    @ConditionalOnProperty(prefix = "admin4j.security.multi", name = "enable", matchIfMissing = true)
    public UsernamePasswordUserDetailsService usernamePasswordUserDetailsService(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            FormLoginProperties formLoginProperties) {

        return new UsernamePasswordUserDetailsService(
                userDetailsService,
                passwordEncoder,
                formLoginProperties
        );
    }
 

    @Bean
    @ConditionalOnMissingBean(IUserContextHolder.class)
    @ConditionalOnClass(name = "com.admin4j.common.service.IUserContextHolder")
    public SecurityUserContextHolder securityUserContextHolder() {
        return new SecurityUserContextHolder();
    }
}
