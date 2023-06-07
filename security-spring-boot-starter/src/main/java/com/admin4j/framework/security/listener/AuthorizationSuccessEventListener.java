package com.admin4j.framework.security.listener;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.security.jwt.JwtUserDetails;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;

/**
 * @author andanyang
 * @since 2023/6/7 9:33
 */
public class AuthorizationSuccessEventListener {

    @EventListener
    public void onAuthorizationSuccess(AuthorizationGrantedEvent event) {
        Object o = event.getAuthentication().get();
        if (o instanceof JwtUserDetails) {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) o;
            AuthenticationUser authenticationUser = new AuthenticationUser();
            authenticationUser.setUserId(jwtUserDetails.getUserId());
            authenticationUser.setTenantId(jwtUserDetails.getTenant());
            authenticationUser.setUsername(jwtUserDetails.getUsername());
            authenticationUser.setAdmin(jwtUserDetails.isAdmin());

            UserContextUtil.setUser(authenticationUser);
        }
    }
}
