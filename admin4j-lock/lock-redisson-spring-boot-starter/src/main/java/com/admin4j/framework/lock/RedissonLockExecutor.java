package com.admin4j.framework.lock;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/4/18 11:07
 */
@RequiredArgsConstructor
public class RedissonLockExecutor implements LockExecutor<RLock> {


    private final RedissonClient redissonClient;


    /**
     * 加锁,获取到锁会block，直到解锁
     *
     * @param lockInfo 锁信息
     */
    @Override
    public void lock(LockInfo<RLock> lockInfo) {

        RLock lock = lockInfo.getLockInstance();
        lock.lock(lockInfo.getLeaseTime(), TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁，不会阻塞
     *
     * @param lockInfo
     * @return true 获取成功，false 获取锁失败
     */
    @Override
    @SneakyThrows
    public boolean tryLock(LockInfo<RLock> lockInfo) {
        RLock lock = lockInfo.getLockInstance();
        return lock.tryLock(lockInfo.getWaitTimeOutSeconds(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);

    }

    /**
     * 解锁
     *
     * @param lockInfo
     */
    @Override
    public void unlock(LockInfo<RLock> lockInfo) {

        RLock lock = lockInfo.getLockInstance();
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    @Override
    public RLock getLock(LockInfo<RLock> lockInfo) {


        switch (lockInfo.getLockModel()) {
            case FAIR:
                //公平锁
                return redissonClient.getFairLock(lockInfo.getLockKey());
            case READ:
                //读之前加读锁，读锁的作用就是等待该lockkey释放写锁以后再读
                RReadWriteLock readLock = redissonClient.getReadWriteLock(lockInfo.getLockKey());
                return readLock.readLock();
            case WRITE:
                //写之前加写锁，写锁加锁成功，读锁只能等待
                RReadWriteLock writeLock = redissonClient.getReadWriteLock(lockInfo.getLockKey());
                return writeLock.writeLock();
            case REENTRANT:
            default:
                //可重入锁
                return redissonClient.getLock(lockInfo.getLockKey());
        }
    }
}
