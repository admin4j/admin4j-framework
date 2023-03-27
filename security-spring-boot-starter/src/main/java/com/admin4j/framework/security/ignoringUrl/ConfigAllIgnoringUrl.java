package com.admin4j.framework.security.ignoringUrl;

import com.admin4j.framework.security.ISecurityIgnoringUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 根据配置文件加载所有请求方式的忽略认证的url
 *
 * @author andanyang
 * @since 2023/3/24 16:59
 */
@ConditionalOnProperty(prefix = "admin4j.security.ignoring-url")
public class ConfigAllIgnoringUrl implements ISecurityIgnoringUrl {

    @Value("${admin4j.security.ignoring-url}")
    private String[] ignoring;

    @Override
    public String[] ignoringUrls() {
        return ignoring;
    }
}
