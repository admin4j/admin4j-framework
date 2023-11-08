package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.AuthenticationResult;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


public class SecurityHandlerConfiguration {
    @Bean
    @ConditionalOnMissingBean({AuthenticationResult.class})
    public AuthenticationResult authenticationResult(UserTokenService userTokenService) {
        return new DefaultAuthenticationResult(userTokenService);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(AuthenticationResult authenticationResult) {
        return new RestAuthenticationEntryPoint(authenticationResult);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler(AuthenticationResult authenticationResult) {
        return new RestAuthenticationFailureHandler(authenticationResult);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler(AuthenticationResult authenticationResult) {
        return new RestAuthenticationSuccessHandler(authenticationResult);
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler(AuthenticationResult authenticationResult) {
        return new RestAccessDeniedHandler(authenticationResult);
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler(AuthenticationResult authenticationResult) {
        return new RestLogoutSuccessHandler(authenticationResult);
    }

    @Bean
    @ConditionalOnClass(name = {"io.jsonwebtoken.SignatureException"})
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }
}
