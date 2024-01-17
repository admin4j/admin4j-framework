package com.admin4j.framework.lock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author andanyang
 * @since 2024/1/16 15:48
 */
@Slf4j
public class LocalLockExecutor implements LockExecutor<Lock> {

    private static final Map<String, Lock> LOCK_MAP = new ConcurrentHashMap<>(32);

    /**
     * 根据锁信息获取锁
     *
     * @param lockInfo
     * @return 获取锁实例
     */
    @Override
    public void setLockInstance(LockInfo lockInfo) {

        // TODO ADD POOL
        Lock lock = LOCK_MAP.computeIfAbsent(lockInfo.getLockKey(), key -> {

            switch (lockInfo.getLockModel()) {
                case FAIR:
                    // 公平锁
                    return new ReentrantLock(true);
                case READ:
                    // 读之前加读锁，读锁的作用就是等待该lockkey释放写锁以后再读
                    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
                    return reentrantReadWriteLock.readLock();
                case WRITE:
                    // 写之前加写锁，写锁加锁成功，读锁只能等待
                    ReentrantReadWriteLock reentrantReadWriteLock1 = new ReentrantReadWriteLock();
                    return reentrantReadWriteLock1.writeLock();
                case REENTRANT:
                default:
                    // 可重入锁
                    return new ReentrantLock();
            }
        });

        lockInfo.setLockInstance(lock);
    }

    /**
     * 加锁,获取到锁会block，直到解锁
     *
     * @param lockInfo 锁信息
     */
    @SneakyThrows
    @Override
    public void lock(LockInfo lockInfo) {
        Lock lock = (Lock) lockInfo.getLockInstance();
        if (lockInfo.getWaitTimeOutSeconds() == -1) {
            lock.lock();
        } else {
            lock.tryLock(lockInfo.getWaitTimeOutSeconds(), TimeUnit.SECONDS);
        }

        log.debug("local Lock success {}", lockInfo.getLockKey());
    }

    /**
     * 尝试获取锁，不会阻塞
     *
     * @param lockInfo
     * @return true 获取成功，false 获取锁失败
     */
    @SneakyThrows
    @Override
    public boolean tryLock(LockInfo lockInfo) {
        Lock lock = (Lock) lockInfo.getLockInstance();
        boolean tryLock = lock.tryLock(lockInfo.getWaitTimeOutSeconds(), TimeUnit.SECONDS);
        if (tryLock) {
            log.debug("local tryLock success {}", lockInfo.getLockKey());
        } else {
            log.debug("local tryLock failed {}", lockInfo.getLockKey());
        }
        return tryLock;
    }

    /**
     * 解锁
     *
     * @param lockInfo
     */
    @Override
    public void unlock(LockInfo lockInfo) {
        Lock lock = (Lock) lockInfo.getLockInstance();
        lock.unlock();

        log.debug("local UnLock success {}", lockInfo.getLockKey());
    }
}
