package com.admin4j.framework.signature.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Actuator 监控授权配置
 *
 * @author zhougang
 * @since 2023/11/12 10:57
 */
@Data
@ConfigurationProperties(prefix = "admin4j.signature")
public class SignatureProperties {

    /**
     * 是否开启
     */
    private boolean enabled = false;

    /**
     * nonce缓存key
     */
    private String signatureNonceCacheKey = "signature:nonce:";

    /**
     * 同一个请求多长时间内有效 默认10分钟
     */
    private Long expireTime = 600000L;
}
