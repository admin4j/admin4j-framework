package com.admin4j.framework.security;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.service.impl.TtlUserContextHolder;
import com.admin4j.framework.security.factory.AuthenticationUserFactory;
import com.admin4j.framework.security.jwt.JwtUserDetails;
import com.admin4j.framework.security.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Objects;

/**
 * @author andanyang
 * @since 2024/1/23 11:41
 */

public class SecurityUserContextHolder extends TtlUserContextHolder {
    private final ObjectProvider<JwtUserDetailsService> jwtUserDetailsServiceObjectProvider;

    public SecurityUserContextHolder(ObjectProvider<JwtUserDetailsService> jwtUserDetailsServiceObjectProvider) {
        super();
        this.jwtUserDetailsServiceObjectProvider = jwtUserDetailsServiceObjectProvider;
    }

    /**
     * 切换用户
     *
     * @param userId
     */
    @Override
    public void setUserId(Long userId) {

        if (Objects.equals(userId, getUserId())) {
            return;
        }
        JwtUserDetailsService jwtUserDetailsService = jwtUserDetailsServiceObjectProvider.getIfAvailable();
        if (jwtUserDetailsService != null) {
            JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUserId(userId);
            AuthenticationUser authenticationUser = AuthenticationUserFactory.getByJwtUser(jwtUserDetails);
            setAuthenticationUser(authenticationUser);
        } else {
            super.setUserId(userId);
        }

    }


}
