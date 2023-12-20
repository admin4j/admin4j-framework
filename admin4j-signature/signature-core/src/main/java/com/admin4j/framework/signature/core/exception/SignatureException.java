package com.admin4j.framework.signature.core.exception;

/**
 * @author zhougang
 * @since 2023/11/10 17:05
 */
public class SignatureException extends RuntimeException {

    public SignatureException(String message) {
        super(message);
    }

    public SignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
