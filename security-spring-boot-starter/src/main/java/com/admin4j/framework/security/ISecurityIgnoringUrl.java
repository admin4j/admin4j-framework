package com.admin4j.framework.security;

import org.springframework.http.HttpMethod;

/**
 * 忽略 Security 认证的url
 *
 * @author andanyang
 * @since 2023/3/24 16:51
 */
public interface ISecurityIgnoringUrl {

    /**
     * 支持的请求方式
     *
     * @return
     */
    default HttpMethod support() {
        return null;
    }

    String[] ignoringUrls();
}
