package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.AuthenticationHandler;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


public class SecurityHandlerConfiguration {
    @Bean
    @ConditionalOnMissingBean({AuthenticationHandler.class})
    public AuthenticationHandler authenticationResult(UserTokenService userTokenService) {
        return new DefaultAuthenticationHandler(userTokenService);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(AuthenticationHandler authenticationHandler) {
        return new RestAuthenticationEntryPoint(authenticationHandler);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler(AuthenticationHandler authenticationHandler) {
        return new RestAuthenticationFailureHandler(authenticationHandler);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler(AuthenticationHandler authenticationHandler) {
        return new RestAuthenticationSuccessHandler(authenticationHandler);
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler(AuthenticationHandler authenticationHandler) {
        return new RestAccessDeniedHandler(authenticationHandler);
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler(AuthenticationHandler authenticationHandler) {
        return new RestLogoutSuccessHandler(authenticationHandler);
    }

    @Bean
    // @ConditionalOnClass(name = {"io.jsonwebtoken.SignatureException"})
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }
}
