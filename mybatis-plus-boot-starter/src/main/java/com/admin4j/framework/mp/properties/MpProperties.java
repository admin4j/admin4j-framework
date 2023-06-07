package com.admin4j.framework.mp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/6/6 15:44
 */
@Data
@ConfigurationProperties(prefix = "admin4j.mp")
public class MpProperties {

    /**
     * 多租需要忽略的表
     */
    private String[] ignoreTenantTable;
    /**
     * 是否开启 sql 性能规范插件
     */
    private boolean illegalSql = false;

}
