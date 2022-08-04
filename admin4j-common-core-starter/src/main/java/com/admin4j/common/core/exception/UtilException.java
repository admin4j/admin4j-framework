package com.admin4j.common.core.exception;

/**
 * 工具类异常
 *
 * @author andanyang
 */
public class UtilException extends BaseException {
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
