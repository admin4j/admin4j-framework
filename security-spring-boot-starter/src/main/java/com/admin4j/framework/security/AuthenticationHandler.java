package com.admin4j.framework.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证成功结果回调处理
 *
 * @author andanyang
 * @since 2023/5/31 17:57
 */
public interface AuthenticationHandler {

    /**
     * 认证成功回调
     */
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    /**
     * 认证失败回调
     */
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception exception);

    /**
     * 退出成功回调
     */
    void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);


    /**
     * 没有权限访问时
     */
    void accessDeniedHandler(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e);


    /**
     * 未登录或登录过期 token 认证失败
     */
    void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException);

}
