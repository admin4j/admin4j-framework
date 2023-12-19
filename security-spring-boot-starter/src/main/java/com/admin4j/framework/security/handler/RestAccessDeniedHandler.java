package com.admin4j.framework.security.handler;

import com.admin4j.framework.security.AuthenticationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2023/3/24 16:34
 * 自定义返回结果：没有权限访问时
 */
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    final AuthenticationHandler authenticationHandler;

    @Override
    public void handle(
            HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {


        authenticationHandler.accessDeniedHandler(httpServletRequest, response, e);
    }
}
