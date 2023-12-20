package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.filter.JwtAuthenticationTokenFilter;
import com.admin4j.framework.security.multi.MultiAuthenticationFilter;
import com.admin4j.framework.security.multi.MultiSecurityConfigurerAdapter;
import com.admin4j.framework.security.properties.FormLoginProperties;
import com.admin4j.framework.security.properties.MultiAuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 多渠道配置自动登录
 *
 * @author andanyang
 * @since 2023/12/15 16:15
 */
@ConditionalOnProperty(prefix = "admin4j.security.multi", value = "enable", matchIfMissing = true)
public class MultiAuthenticationAutoConfiguration {

    @Autowired
    MultiAuthenticationProperties multiAuthenticationProperties;
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    FormLoginProperties formLoginProperties;


    @Bean
    @ConditionalOnMissingBean(MultiAuthenticationFilter.class)
    public MultiAuthenticationFilter multiAuthenticationFilter(AuthenticationManager authenticationManager) {

        MultiAuthenticationFilter authenticationFilter = new MultiAuthenticationFilter(multiAuthenticationProperties, formLoginProperties);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return authenticationFilter;
    }

    /**
     * 多渠道认证功能配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MultiSecurityConfigurerAdapter.class)
    public MultiSecurityConfigurerAdapter multiSecurityConfigurerAdapter(MultiAuthenticationFilter multiAuthenticationFilter,
                                                                         JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                                                                         AuthenticationManager authenticationManager) throws Exception {


        return new MultiSecurityConfigurerAdapter(multiAuthenticationFilter, authenticationManager, jwtAuthenticationTokenFilter);
    }
}
