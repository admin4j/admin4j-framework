package com.admin4j.framework.lock;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/4/18 11:07
 */
@RequiredArgsConstructor
@Slf4j
public class RedissonLockExecutor extends AbstractParentLockExecutor<RLock> {


    private final RedissonClient redissonClient;


    /**
     * 加锁,获取到锁会block，直到解锁
     *
     * @param lockInfo 锁信息
     */
    @Override
    protected void lockSelf(LockInfo lockInfo) {

        RLock lock = (RLock) lockInfo.getLockInstance();
        lock.lock(lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        log.debug("redisson Lock success {}", lockInfo.getLockKey());
    }

    /**
     * 尝试获取锁，不会阻塞
     *
     * @param lockInfo
     * @return true 获取成功，false 获取锁失败
     */
    @Override
    @SneakyThrows
    protected boolean tryLockSelf(LockInfo lockInfo) {
        RLock lock = (RLock) lockInfo.getLockInstance();
        boolean tryLock = lock.tryLock(lockInfo.getWaitTimeOutSeconds(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        if (tryLock) {
            log.debug("redisson tryLock success {}", lockInfo.getLockKey());
        } else {
            log.debug("redisson tryLock failed {}", lockInfo.getLockKey());
        }
        return tryLock;
    }

    /**
     * 解锁
     *
     * @param lockInfo
     */
    @Override
    protected void unlockSelf(LockInfo lockInfo) {

        RLock lock = (RLock) lockInfo.getLockInstance();
        if (lock.isLocked()) {
            lock.unlock();
        }
        log.debug("redisson UnLock success {}", lockInfo.getLockKey());
    }

    protected RLock getLockInstanceSelf(LockInfo lockInfo) {

        switch (lockInfo.getLockModel()) {
            case FAIR:
                // 公平锁
                return redissonClient.getFairLock(lockInfo.getLockKey());
            case READ:
                // 读之前加读锁，读锁的作用就是等待该lockkey释放写锁以后再读
                RReadWriteLock readLock = redissonClient.getReadWriteLock(lockInfo.getLockKey());
                return readLock.readLock();
            case WRITE:
                // 写之前加写锁，写锁加锁成功，读锁只能等待
                RReadWriteLock writeLock = redissonClient.getReadWriteLock(lockInfo.getLockKey());
                return writeLock.writeLock();
            case REENTRANT:
            default:
                // 可重入锁
                return redissonClient.getLock(lockInfo.getLockKey());
        }
    }
}
