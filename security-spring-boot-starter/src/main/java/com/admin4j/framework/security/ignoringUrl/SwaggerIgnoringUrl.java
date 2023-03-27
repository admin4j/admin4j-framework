package com.admin4j.framework.security.ignoringUrl;

import com.admin4j.framework.security.ISecurityIgnoringUrl;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * Swagger 框架忽略认证的url
 *
 * @author andanyang
 * @since 2023/3/24 16:54
 */
@ConditionalOnClass(Api.class)
public class SwaggerIgnoringUrl implements ISecurityIgnoringUrl {

    private static final String[] IGNORING_URL = new String[]{"/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/druid/**"};

    @Override
    public String[] ignoringUrls() {
        return IGNORING_URL;
    }
}
