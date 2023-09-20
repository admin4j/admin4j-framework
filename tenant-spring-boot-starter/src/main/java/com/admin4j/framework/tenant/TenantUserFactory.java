package com.admin4j.framework.tenant;

import com.admin4j.common.pojo.AuthenticationUser;

/**
 * @author andanyang
 * @since 2023/9/15 9:04
 */
public class TenantUserFactory {

    public static AuthenticationUser jobUser(Long tenantId) {

        AuthenticationUser authenticationUser = new AuthenticationUser();
        authenticationUser.setUserId(1000L);
        authenticationUser.setUsername("job");
        authenticationUser.setTenantId(tenantId);
        authenticationUser.setAdmin(true);
        return authenticationUser;
    }

    public static AuthenticationUser mqUser(Long tenantId) {

        AuthenticationUser authenticationUser = new AuthenticationUser();
        authenticationUser.setUserId(1001L);
        authenticationUser.setUsername("mq");
        authenticationUser.setTenantId(tenantId);
        authenticationUser.setAdmin(true);
        return authenticationUser;
    }
}
