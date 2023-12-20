package com.admin4j.framework.tenant.autoconfigure;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.framework.tenant.openfeign.UserContextHolderFilter;
import com.admin4j.framework.tenant.prop.TenantProperties;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author andanyang
 * @since 2023/9/20 9:42
 */
@AutoConfigureOrder(Integer.MAX_VALUE)
@EnableConfigurationProperties(TenantProperties.class)
public class TTenantFilterAutoConfiguration {

    @Bean
    @ConditionalOnBean(IUserContextHolder.class)
    @Order
    @ConditionalOnProperty(value = "admin4j.tenant.servletFilter", matchIfMissing = true)
    public UserContextHolderFilter userContextHolderFilter(IUserContextHolder userContextHolder) {
        return new UserContextHolderFilter(userContextHolder);
    }
}
