package com.admin4j.limiter.key;

import com.admin4j.common.service.ILoginUserInfoService;
import com.admin4j.limiter.core.RateLimiterKeyGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author andanyang
 * @since 2023/5/12 10:04
 */
@RequiredArgsConstructor
public class UserRateLimiterKeyGenerate implements RateLimiterKeyGenerate {

    private final ILoginUserInfoService loginUserInfoService;

    @Override
    public void generateKey(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, StringBuilder keyBuilder) {

        keyBuilder.append(request.getRequestURI())
                .append(":U")
                .append(loginUserInfoService.getUserId());
    }
}
