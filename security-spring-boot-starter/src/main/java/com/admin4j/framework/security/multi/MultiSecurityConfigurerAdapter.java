package com.admin4j.framework.security.multi;

import com.admin4j.framework.security.filter.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author andanyang
 * @since 2023/6/1 17:55
 */
@RequiredArgsConstructor
public class MultiSecurityConfigurerAdapter extends AbstractHttpConfigurer<MultiSecurityConfigurerAdapter, HttpSecurity> {

    private final MultiAuthenticationFilter multiAuthenticationFilter;

    private final AuthenticationManager authenticationManager;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        multiAuthenticationFilter.setAuthenticationManager(authenticationManager);
        http.authenticationManager(authenticationManager)
                // 添加JWT filter
                .addFilterBefore(jwtAuthenticationTokenFilter, LogoutFilter.class)
                .addFilterBefore(multiAuthenticationFilter, LogoutFilter.class);
    }


}
