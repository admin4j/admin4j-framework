package com.admin4j.lock.exception;

/**
 * @author andanyang
 * @since 2023/3/14 10:12
 */
public class IdempotentException extends RuntimeException {
    private static final long serialVersionUID = -5531624525887909020L;

    public IdempotentException(String msg) {
        super(msg);
    }


    public IdempotentException(Exception e) {
        super(e);
    }
}
