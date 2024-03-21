package com.admin4j.framework.lock.pool;

import com.admin4j.framework.lock.InvalidLockInstance;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 包装锁
 *
 * @author andanyang
 * @since 2024/1/17 17:29
 */
public class WrapperLockObject implements Lock, InvalidLockInstance {


    protected final Object lockObject;
    protected final AtomicInteger count;


    public WrapperLockObject(Object lockObject) {
        this.lockObject = lockObject;
        count = new AtomicInteger(0);
    }

    @Override
    public void invalid() {
        count.set(-10);
    }

    /**
     * 锁实例是否无效
     *
     * @return
     */
    public boolean isInvalid() {
        return count.get() < 0;
    }

    protected Lock getLock() {
        return (Lock) lockObject;
    }

    public int increment() {
        return count.incrementAndGet();
    }

    public int decrement() {
        return count.decrementAndGet();
    }

    public void clear() {
        if (!isInvalid()) {
            count.set(0);
        }

        if (lockObject instanceof ReentrantLock) {
            if (((ReentrantLock) lockObject).isLocked()) {
                ((ReentrantLock) lockObject).unlock();
            }
        }
    }

    @Override
    public void lock() {

        getLock().lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        getLock().lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return getLock().tryLock();
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return getLock().tryLock(l, timeUnit);
    }

    @Override
    public void unlock() {
        getLock().unlock();
    }

    @Override
    public Condition newCondition() {
        return getLock().newCondition();
    }

    public boolean isActive() {

        return count.get() > 0;
    }
}
