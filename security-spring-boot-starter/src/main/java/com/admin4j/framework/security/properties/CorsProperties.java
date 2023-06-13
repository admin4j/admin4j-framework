package com.admin4j.framework.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/6/13 16:27
 */
@Data
@ConfigurationProperties(prefix = "admin4j.kaptcha")
public class CorsProperties {

    /**
     * 它的值是逗号分隔的一个字符串，表明服务器支持的所有跨域请求的方法。
     */
    private String allowedMethod = "*";
    /**
     * 它的值要么是请求时Origin字段的值，要么是一个*，表示接受任意域名的请求。
     */
    private String allowedOrigin = "*";
    /**
     * 它也是一个逗号分隔的字符串，表明服务器支持的所有头信息字段，不限于浏览器在"预检"中请求的字段。
     */
    private String allowedHeader = "*";
    /**
     * 该字段可选，用来指定本次预检请求的有效期，单位为秒。上面结果中，有效期是20天（1728000秒），即允许缓存该条回应1728000秒（即20天），在此期间，不用发出另一条预检请求。
     */
    private Long maxAge = 180000L;
    /**
     * 是否允许客户端带cookie
     */
    private boolean allowCredentials = true;
}
