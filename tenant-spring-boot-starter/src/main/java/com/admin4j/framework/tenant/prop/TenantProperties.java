package com.admin4j.framework.tenant.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/12/1 11:13
 */
@Data
@ConfigurationProperties(prefix = "admin4j.tenant")
public class TenantProperties {


    /**
     * 是否开启 servletFilter 的租户过滤
     */
    private boolean servletFilter = true;
}
