package com.admin4j.framework.security.handler;

import com.admin4j.framework.security.AuthenticationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2023/3/27 15:30
 */
@RequiredArgsConstructor
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

    final AuthenticationHandler authenticationHandler;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        authenticationHandler.onLogoutSuccess(request, response, authentication);
    }
}
