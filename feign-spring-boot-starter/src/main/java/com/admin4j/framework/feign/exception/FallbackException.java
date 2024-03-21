package com.admin4j.framework.feign.exception;

/**
 * @author andanyang
 * @since 2023/12/22 9:37
 */
public class FallbackException extends RuntimeException {
    private static final long serialVersionUID = -3929026284063645278L;

    public FallbackException(Throwable cause) {
        super(cause);
    }

    public FallbackException(String message) {
        super(message);
    }
}
