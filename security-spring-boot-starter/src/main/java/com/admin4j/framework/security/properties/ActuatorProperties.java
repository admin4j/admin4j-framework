package com.admin4j.framework.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Actuator 监控授权配置
 *
 * @author andanyang
 * @since 2023/6/7 10:57
 */
@Data
@ConfigurationProperties(prefix = "admin4j.security.actuator")
public class ActuatorProperties {
    /**
     * 是否开启
     */
    private boolean enabled = false;
    /**
     * 限制IP
     */
    private String[] ips;
}
