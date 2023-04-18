package com.admin4j.framework.lock.exception;

/**
 * @author andanyang
 * @since 2023/4/18 11:27
 */
public class UnSupportException extends RuntimeException {

    public UnSupportException() {

        super("not supported");
    }

    public UnSupportException(String message) {

        super(message);
    }
}
