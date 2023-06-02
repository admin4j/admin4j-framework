package com.admin4j.framework.security.mult;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author andanyang
 * @since 2023/6/1 17:55
 */
@RequiredArgsConstructor
public class MultiSecurityConfigurerAdapter extends AbstractHttpConfigurer<MultiSecurityConfigurerAdapter, HttpSecurity> {

    private final MultiAuthenticationFilter multiAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        multiAuthenticationFilter.setAuthenticationManager(authenticationManager);
        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(multiAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
