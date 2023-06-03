package com.admin4j.framework.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/5/30 13:28
 */
@Data
@ConfigurationProperties(prefix = "admin4j.security.jwt")
public class JwtProperties {
    /**
     * header行里的令牌自定义标识
     */
    private String header = "Authorization";
    /**
     * 令牌前缀
     */
    private String tokenPrefix = "Bearer ";
    /**
     * 令牌秘钥
     */
    private String secret = "Tkhotv8T";
    /**
     * 令牌有效期，单位秒（默认30分钟）
     */
    private int expires = 1800;
}
