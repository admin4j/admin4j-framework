package com.admin4j.limiter.key;

import com.admin4j.common.service.ILoginTenantInfoService;
import com.admin4j.limiter.core.RateLimiterKeyGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

/**
 * @author andanyang
 * @since 2023/5/12 10:04
 */
@RequiredArgsConstructor
public class TenantRateLimiterKeyGenerate implements RateLimiterKeyGenerate {

    private final ILoginTenantInfoService loginTenantInfoService;

    @Override
    public String generateKey(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, HandlerMethod handlerMethod) {


        return request.getRequestURI() + ":T" + loginTenantInfoService.getTenant();
    }
}
