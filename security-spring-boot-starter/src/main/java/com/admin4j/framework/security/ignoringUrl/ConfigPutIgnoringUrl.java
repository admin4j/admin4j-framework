package com.admin4j.framework.security.ignoringUrl;

import com.admin4j.framework.security.ISecurityIgnoringUrl;
import com.admin4j.framework.security.IgnoringUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;

/**
 * 根据配置文件加载忽略认证的url
 *
 * @author andanyang
 * @since 2023/3/24 16:59
 */
@ConditionalOnProperty(prefix = "admin4j.security.ignoring.put")
public class ConfigPutIgnoringUrl implements ISecurityIgnoringUrl {
    @Autowired
    IgnoringUrlProperties ignoringUrlProperties;

    @Override
    public HttpMethod support() {
        return HttpMethod.PUT;
    }

    @Override
    public String[] ignoringUrls() {
        return ignoringUrlProperties.getGet();
    }
}
