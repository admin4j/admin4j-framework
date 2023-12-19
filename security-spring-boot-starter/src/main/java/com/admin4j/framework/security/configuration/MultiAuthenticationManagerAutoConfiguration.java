package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.multi.MultiAuthenticationProvider;
import com.admin4j.framework.security.multi.MultiUserDetailsService;
import com.admin4j.framework.security.multi.UsernamePasswordUserDetailsService;
import com.admin4j.framework.security.properties.FormLoginProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 多渠道配置自动登录
 *
 * @author andanyang
 * @since 2023/12/15 16:15
 */
@ConditionalOnProperty(prefix = "admin4j.security.multi", value = "enable", matchIfMissing = true)
public class MultiAuthenticationManagerAutoConfiguration {

    /**
     * 默认的多渠道表单登录
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(UsernamePasswordUserDetailsService.class)
    @ConditionalOnBean(UserDetailsService.class)
    // @ConditionalOnProperty(prefix = "admin4j.security.multi", name = "enable", matchIfMissing = true)
    @ConditionalOnProperty(prefix = "admin4j.security.form-login", name = "enable", matchIfMissing = true)
    public MultiUserDetailsService usernamePasswordUserDetailsService(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            FormLoginProperties formLoginProperties
    ) {

        return new UsernamePasswordUserDetailsService(
                passwordEncoder,
                formLoginProperties,
                userDetailsService
        );
    }


    /**
     * 获取 PermissionAuthorizationManager
     * <p>
     * 或者：
     * <code>
     * AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
     * authenticationManagerBuilder.userDetailsService(userDetailsService);
     * authenticationManager = authenticationManagerBuilder.build();
     * </code>
     *
     * @return
     */

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager(@Autowired(required = false)
                                                       List<MultiUserDetailsService> userDetailServices) {

        return new ProviderManager(new MultiAuthenticationProvider(userDetailServices));
    }
}
