package com.admin4j.framework.security;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.service.impl.TtlUserContextHolder;
import com.admin4j.framework.security.factory.AuthenticationUserFactory;
import com.admin4j.framework.security.jwt.JwtUserDetails;
import com.admin4j.framework.security.jwt.JwtUserDetailsService;

import java.util.Objects;

/**
 * @author andanyang
 * @since 2024/1/23 11:41
 */

public class SecurityUserContextHolder extends TtlUserContextHolder {
    private final JwtUserDetailsService jwtUserDetailsService;

    public SecurityUserContextHolder(JwtUserDetailsService jwtUserDetails) {
        super();
        this.jwtUserDetailsService = jwtUserDetails;
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
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUserId(userId);
        AuthenticationUser authenticationUser = AuthenticationUserFactory.getByJwtUser(jwtUserDetails);
        setAuthenticationUser(authenticationUser);
    }


}
