package com.admin4j.framework.security.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录成功事件
 *
 * @author andanyang
 * @since 2023/5/31 17:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationSuccessEvent {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Authentication authentication;
}
