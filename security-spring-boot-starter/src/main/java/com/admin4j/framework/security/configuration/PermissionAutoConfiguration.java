package com.admin4j.framework.security.configuration;

import com.admin4j.framework.security.authorization.IPermissionUriService;
import com.admin4j.framework.security.authorization.PermissionAuthorizationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/12/19 14:40
 */
@ConditionalOnBean(IPermissionUriService.class)
@ConditionalOnMissingBean(PermissionAuthorizationManager.class)
public class PermissionAutoConfiguration {

    @Bean
    public PermissionAuthorizationManager permissionAuthorizationManager(IPermissionUriService permissionUriService) {

        return new PermissionAuthorizationManager(permissionUriService);
    }
}
