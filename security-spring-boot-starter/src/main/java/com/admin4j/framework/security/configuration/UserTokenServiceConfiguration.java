package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.mult.UsernamePasswordUserDetailsService;
import com.admin4j.framework.security.properties.FormLoginProperties;
import com.admin4j.framework.security.properties.JWTProperties;
import com.admin4j.framework.security.token.UserJWTTokenService;
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
    public UserTokenService userTokenService(JWTProperties jwtProperties) {
        return new UserJWTTokenService(jwtProperties);
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
    ///**
    // * 自行实现登录逻辑
    // *
    // * @return
    // */
    //@Bean
    //@ConditionalOnMissingBean(UserDetailsService.class)
    //public UserDetailsService defaultUserDetailsService() {
    //    return username -> null;
    //}
}
