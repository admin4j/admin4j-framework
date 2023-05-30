package com.admin4j.framework.security.ignoringUrl;

import com.admin4j.framework.security.ISecurityIgnoringUrl;
import org.springframework.http.HttpMethod;

/**
 * 默认忽略认证的url
 *
 * @author andanyang
 * @since 2023/3/24 16:54
 */

public class DefaultIgnoringUrl implements ISecurityIgnoringUrl {

    private static final String[] IGNORING_URL = new String[]{"/favicon.ico", "/error"};

    @Override
    public HttpMethod support() {
        return HttpMethod.GET;
    }

    @Override
    public String[] ignoringUrls() {
        return IGNORING_URL;
    }
}
