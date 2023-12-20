package com.admin4j.common.exception;

/**
 * @author andanyang
 * @since 2023/9/19 10:06
 */
public class EncryptException extends SystemException {

    public EncryptException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EncryptException(Throwable throwable) {
        super(throwable);
    }
}
