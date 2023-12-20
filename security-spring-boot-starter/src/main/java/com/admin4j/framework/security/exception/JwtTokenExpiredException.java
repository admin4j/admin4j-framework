package com.admin4j.framework.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author andanyang
 * @since 2023/11/22 9:38
 */
public class JwtTokenExpiredException extends AuthenticationException {
    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public JwtTokenExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public JwtTokenExpiredException(String msg) {
        super(msg);
    }
}
