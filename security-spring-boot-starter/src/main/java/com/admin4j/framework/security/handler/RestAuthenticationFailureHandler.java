package com.admin4j.framework.security.handler;

import com.admin4j.framework.security.AuthenticationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2023/5/31 17:54
 */
@RequiredArgsConstructor
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    final AuthenticationHandler authenticationHandler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        authenticationHandler.onAuthenticationFailure(request, response, exception);
    }
}
