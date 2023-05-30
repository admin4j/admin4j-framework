package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.ISecurityIgnoringUrl;
import com.admin4j.framework.security.filter.JwtAuthenticationTokenFilter;
import com.admin4j.framework.security.ignoringUrl.AnonymousAccessUrl;
import com.admin4j.framework.security.properties.IgnoringUrlProperties;
import com.admin4j.framework.security.properties.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.List;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/3/24 16:34
 */

/**
 * 开启方法级别的注解支持
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({IgnoringUrlProperties.class, JWTProperties.class})
public class SecurityConfiguration {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired(required = false)
    List<ISecurityIgnoringUrl> securityIgnoringUrls;
    @Autowired(required = false)
    IgnoringUrlProperties ignoringUrlProperties;
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    @Autowired
    AnonymousAccessUrl anonymousAccessUrl;
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable()
                .and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 认证失败处理类
                .exceptionHandling()
                // 未登录或登录过期
                .authenticationEntryPoint(authenticationEntryPoint)
                // 没有权限访问时
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();

        // 添加Logout filter
        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        //httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        //httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {

            //设置匿名访问url
            WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();

            if (securityIgnoringUrls != null && !securityIgnoringUrls.isEmpty()) {
                securityIgnoringUrls.forEach(url -> {

                    if (url.ignoringUrls() == null || url.ignoringUrls().length == 0) {
                        return;
                    }

                    if (url.support() == null) {
                        ignoring.antMatchers(url.ignoringUrls());
                    } else {
                        ignoring.antMatchers(url.support(), url.ignoringUrls());
                    }
                });
            }

            if (ignoringUrlProperties != null) {

                if (ignoringUrlProperties.getUris() != null && ignoringUrlProperties.getUris().length > 0) {
                    ignoring.antMatchers(ignoringUrlProperties.getUris());
                }
                if (ignoringUrlProperties.getGet() != null && ignoringUrlProperties.getGet().length > 0) {
                    ignoring.antMatchers(HttpMethod.GET, ignoringUrlProperties.getGet());
                }

                if (ignoringUrlProperties.getPost() != null && ignoringUrlProperties.getPost().length > 0) {
                    ignoring.antMatchers(HttpMethod.POST, ignoringUrlProperties.getPost());
                }
                if (ignoringUrlProperties.getPut() != null && ignoringUrlProperties.getPut().length > 0) {
                    ignoring.antMatchers(HttpMethod.PUT, ignoringUrlProperties.getPut());
                }
                if (ignoringUrlProperties.getPatch() != null && ignoringUrlProperties.getPatch().length > 0) {
                    ignoring.antMatchers(HttpMethod.PATCH, ignoringUrlProperties.getPatch());
                }
                if (ignoringUrlProperties.getDelete() != null && ignoringUrlProperties.getDelete().length > 0) {
                    ignoring.antMatchers(HttpMethod.DELETE, ignoringUrlProperties.getDelete());
                }
            }

            //AnonymousAccess 注解
            if (anonymousAccessUrl != null) {

                Map<HttpMethod, String[]> anonymousUrl = anonymousAccessUrl.getAnonymousUrl();
                anonymousUrl.keySet().forEach(i -> {

                    ignoring.antMatchers(i, anonymousUrl.get(i));
                });
            }
        };
    }


}
