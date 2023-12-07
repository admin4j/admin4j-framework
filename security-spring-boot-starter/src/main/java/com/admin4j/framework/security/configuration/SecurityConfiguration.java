package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.ISecurityIgnoringUrl;
import com.admin4j.framework.security.filter.ActuatorFilter;
import com.admin4j.framework.security.filter.JwtAuthenticationTokenFilter;
import com.admin4j.framework.security.ignoringUrl.AnonymousAccessUrl;
import com.admin4j.framework.security.mult.MultiAuthenticationFilter;
import com.admin4j.framework.security.mult.MultiAuthenticationProvider;
import com.admin4j.framework.security.mult.MultiSecurityConfigurerAdapter;
import com.admin4j.framework.security.mult.MultiUserDetailsService;
import com.admin4j.framework.security.properties.FormLoginProperties;
import com.admin4j.framework.security.properties.IgnoringUrlProperties;
import com.admin4j.framework.security.properties.JwtProperties;
import com.admin4j.framework.security.properties.MultiAuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.Map;

/**
 * TODO 需要注入，取消 UserDetailsServiceAutoConfiguration 开启
 * 		value = { AuthenticationManager.class, AuthenticationProvider.class, UserDetailsService.class,
 * 				AuthenticationManagerResolver.class },
 *
 * @author andanyang
 * @since 2023/3/24 16:34
 */

/**
 * 开启方法级别的注解支持
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({IgnoringUrlProperties.class, JwtProperties.class, FormLoginProperties.class, MultiAuthenticationProperties.class})
@AutoConfigureBefore(UserDetailsServiceAutoConfiguration.class)
public class SecurityConfiguration {


    @Autowired(required = false)
    List<ISecurityIgnoringUrl> securityIgnoringUrls;
    @Autowired(required = false)
    IgnoringUrlProperties ignoringUrlProperties;
    @Autowired
    FormLoginProperties formLoginProperties;
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    AnonymousAccessUrl anonymousAccessUrl;
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired(required = false)
    List<UsernamePasswordAuthenticationFilter> usernamePasswordAuthenticationFilters;
    @Autowired
    MultiAuthenticationProperties multiAuthenticationProperties;
    @Autowired(required = false)
    List<MultiUserDetailsService> userDetailServices;
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;
    @Autowired(required = false)
    ActuatorFilter actuatorFilter;
    @Autowired(required = false)
    CorsFilter corsFilter;


    /**
     * 取消ROLE_前缀
     */
    //@Bean
    // public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    //    // Remove the ROLE_ prefix
    //    return new GrantedAuthorityDefaults("");
    //}

    // /**
    //  * 设置中文配置
    //  */
    // @Bean
    // public ReloadableResourceBundleMessageSource messageSource() {
    //     ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    //     messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
    //     return messageSource;
    // }

    /**
     * 安全配置
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // FilterSecurityInterceptor
        httpSecurity
                // 关闭cors
                .cors().disable()
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers()
                .cacheControl().disable()
                .frameOptions().disable()
                .and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 认证失败处理类 异常处理
                .exceptionHandling()
                // 未登录或登录过期
                .authenticationEntryPoint(authenticationEntryPoint)
                // 没有权限访问时
                .accessDeniedHandler(accessDeniedHandler)
        ;

        // 添加Logout filter
        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        if (corsFilter != null) {
            httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        }


        if (usernamePasswordAuthenticationFilters != null && usernamePasswordAuthenticationFilters.size() > 0) {
            usernamePasswordAuthenticationFilters.forEach(usernameFilter -> httpSecurity.addFilterBefore(usernameFilter, UsernamePasswordAuthenticationFilter.class));
        }


        authorizeRequestsConfigurer(httpSecurity);

        // 多渠道登录
        if (multiAuthenticationProperties.isEnable()) {

            MultiAuthenticationFilter authenticationFilter = new MultiAuthenticationFilter(multiAuthenticationProperties, formLoginProperties);

            authenticationFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
            authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
            authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);

            MultiSecurityConfigurerAdapter multiSecurityConfigurerAdapter = new MultiSecurityConfigurerAdapter(authenticationFilter, new MultiAuthenticationProvider(userDetailServices));
            httpSecurity.apply(multiSecurityConfigurerAdapter);

        } else {
            // 开启form表单认证
            httpSecurity.formLogin()
                    .loginProcessingUrl(formLoginProperties.getLoginProcessingUrl())
                    .passwordParameter(formLoginProperties.getPasswordParameter())
                    .usernameParameter(formLoginProperties.getUsernameParameter())
                    .failureHandler(authenticationFailureHandler)
                    .successHandler(authenticationSuccessHandler)
                    .permitAll();

        }

        if (actuatorFilter != null) {
            httpSecurity.addFilterBefore(actuatorFilter, UsernamePasswordAuthenticationFilter.class);
        }

        // GlobalAuthenticationConfigurerAdapter
        // WebSecurityConfigurerAdapter
        return httpSecurity.build();
    }

    /**
     * 授权请求配置
     */
    private void authorizeRequestsConfigurer(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
                httpSecurity.authorizeRequests();
        // 忽略URl配置
        ignoringRequestMatcherRegistry(expressionInterceptUrlRegistry);
        // 除上面外的所有请求全部需要鉴权认证;其他路径必须验证
        expressionInterceptUrlRegistry.anyRequest().authenticated();
    }

    /**
     * 忽略URl配置
     */
    private void ignoringRequestMatcherRegistry(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {

        if (securityIgnoringUrls != null && !securityIgnoringUrls.isEmpty()) {
            securityIgnoringUrls.forEach(url -> {

                if (url.ignoringUrls() == null || url.ignoringUrls().length == 0) {
                    return;
                }

                if (url.support() == null) {
                    expressionInterceptUrlRegistry.antMatchers(url.ignoringUrls()).permitAll();
                } else {
                    expressionInterceptUrlRegistry.antMatchers(url.support(), url.ignoringUrls()).permitAll();
                }
            });
        }

        if (ignoringUrlProperties != null) {

            if (ignoringUrlProperties.getUris() != null && ignoringUrlProperties.getUris().length > 0) {
                expressionInterceptUrlRegistry.antMatchers(ignoringUrlProperties.getUris()).permitAll();
            }
            if (ignoringUrlProperties.getGet() != null && ignoringUrlProperties.getGet().length > 0) {
                expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, ignoringUrlProperties.getGet()).permitAll();
            }

            if (ignoringUrlProperties.getPost() != null && ignoringUrlProperties.getPost().length > 0) {
                expressionInterceptUrlRegistry.antMatchers(HttpMethod.POST, ignoringUrlProperties.getPost()).permitAll();
            }
            if (ignoringUrlProperties.getPut() != null && ignoringUrlProperties.getPut().length > 0) {
                expressionInterceptUrlRegistry.antMatchers(HttpMethod.PUT, ignoringUrlProperties.getPut()).permitAll();
            }
            if (ignoringUrlProperties.getPatch() != null && ignoringUrlProperties.getPatch().length > 0) {
                expressionInterceptUrlRegistry.antMatchers(HttpMethod.PATCH, ignoringUrlProperties.getPatch()).permitAll();
            }
            if (ignoringUrlProperties.getDelete() != null && ignoringUrlProperties.getDelete().length > 0) {
                expressionInterceptUrlRegistry.antMatchers(HttpMethod.DELETE, ignoringUrlProperties.getDelete()).permitAll();
            }
        }

        // AnonymousAccess 注解
        if (anonymousAccessUrl != null) {

            Map<HttpMethod, String[]> anonymousUrl = anonymousAccessUrl.getAnonymousUrl();
            anonymousUrl.keySet().forEach(i -> {

                expressionInterceptUrlRegistry.antMatchers(i, anonymousUrl.get(i)).permitAll();
            });
        }
    }
}
