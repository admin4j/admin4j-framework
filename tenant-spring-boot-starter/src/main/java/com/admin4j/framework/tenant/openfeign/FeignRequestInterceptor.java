package com.admin4j.framework.tenant.openfeign;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.util.ServletUtils;
import com.admin4j.framework.tenant.TenantConstant;
import com.admin4j.spring.util.IpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * feign 请求拦截器
 */
@Slf4j
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {


    private final IUserContextHolder userContextHolder;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        if (userContextHolder.isLogin()) {
            // 传递用户信息请求头，防止丢失
            String encode = userContextHolder.encode();
            try {
                requestTemplate.header(TenantConstant.USER_INFO, URLEncoder.encode(encode, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        // 配置客户端IP
        javax.servlet.http.HttpServletRequest request = ServletUtils.getRequest();
        requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(request));
    }
}