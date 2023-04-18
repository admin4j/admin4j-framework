package com.admin4j.framework.lock.exception;

/**
 * 分布式锁异常
 *
 * @author andanyang
 * @since 2022/7/15 14:04
 */
public class DistributedLockException extends RuntimeException {

    private static final long serialVersionUID = -5531624525887909020L;

    public DistributedLockException(String msg) {
        super(msg);
    }


    public DistributedLockException(Exception e) {
        super(e);
    }
}
