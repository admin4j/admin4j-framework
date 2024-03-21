package com.admin4j.framework.lock.pool;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author andanyang
 * @since 2024/1/17 18:13
 */
@Setter
@Getter
public class WrapperReadWriteLockObject extends WrapperLockObject {

    private boolean isRead = false;

    public WrapperReadWriteLockObject(Object lockObject) {
        super(lockObject);
    }

    @Override
    protected Lock getLock() {
        ReadWriteLock readWriteLock = (ReadWriteLock) super.lockObject;
        return isRead ? readWriteLock.readLock() : readWriteLock.writeLock();
    }

    @Override
    public void clear() {

        super.count.set(0);
    }
}
