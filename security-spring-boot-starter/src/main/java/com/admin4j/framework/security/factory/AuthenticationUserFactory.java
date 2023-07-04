package com.admin4j.framework.security.factory;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.framework.security.jwt.JwtUserDetails;

/**
 * @author andanyang
 * @since 2023/7/4 8:53
 */
public class AuthenticationUserFactory {

    public static AuthenticationUser getByJwtUser(JwtUserDetails jwtUserDetails) {

        AuthenticationUser authenticationUser = new AuthenticationUser();
        authenticationUser.setUserId(jwtUserDetails.getUserId());
        authenticationUser.setTenantId(jwtUserDetails.getTenantId());
        authenticationUser.setUsername(jwtUserDetails.getUsername());
        authenticationUser.setAdmin(jwtUserDetails.isAdmin());
        authenticationUser.setPermissions(jwtUserDetails.getPermissions());
        authenticationUser.setOriginAuthentication(jwtUserDetails);
        return authenticationUser;
    }
}
