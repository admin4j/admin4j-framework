package com.admin4j.job.exception;

/**
 * @author andanyang
 * @since 2023/5/18 16:24
 */
public class JobException extends RuntimeException {

    public JobException(String message) {
        super(message);
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }
}
