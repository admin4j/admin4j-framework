package com.admin4j.framework.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 表单登录配置
 *
 * @author andanyang
 * @since 2023/5/30 13:28
 */
@Data
@ConfigurationProperties(prefix = "admin4j.security.form-login")
public class FormLoginProperties {
    /**
     * 开启form 处理 url
     */
    private String loginProcessingUrl = "/login";
    /**
     * 密码字段名
     */
    private String passwordParameter = "password";
    /**
     * 账号字段名
     */
    private String usernameParameter = "username";
}
