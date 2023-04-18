package com.admin4j.framework.lock;

import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.framework.lock.exception.UnSupportException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/4/18 11:25
 */
@RequiredArgsConstructor
public class ZookeeperLockExecutor implements LockExecutor<InterProcessLock> {

    private final CuratorFramework curatorFramework;

    /**
     * 根据锁信息获取锁
     *
     * @param lockInfo
     * @return 获取锁实例
     */
    @Override
    public InterProcessLock getLock(LockInfo<InterProcessLock> lockInfo) {
        InterProcessReadWriteLock interProcessReadWriteLock;
        String lockKey = lockInfo.getLockKey();
        if (!StringUtils.startsWith(lockKey, "/")) {
            lockKey = "/" + lockKey;
        }
        switch (lockInfo.getLockModel()) {
            case FAIR:
                //公平锁
                throw new UnSupportException("Zookeeper Not supported FAIR Lock");
            case READ:
                interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework, lockKey);
                return interProcessReadWriteLock.readLock();
            case WRITE:
                //写之前加写锁，写锁加锁成功，读锁只能等待
                interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework, lockKey);
                return interProcessReadWriteLock.writeLock();
            case REENTRANT:
            default:
                //可重入锁
                return new InterProcessMutex(curatorFramework, lockKey);
        }
    }

    /**
     * 加锁,获取到锁会block，直到解锁
     *
     * @param lockInfo 锁信息
     * @return 锁信息
     */
    @Override
    @SneakyThrows
    public void lock(LockInfo<InterProcessLock> lockInfo) {
        lockInfo.getLockInstance().acquire();
    }

    /**
     * 尝试获取锁，不会阻塞
     *
     * @param lockInfo
     * @return true 获取成功，false 获取锁失败
     */
    @Override
    @SneakyThrows
    public boolean tryLock(LockInfo<InterProcessLock> lockInfo) {
        return lockInfo.getLockInstance().acquire(-1, TimeUnit.SECONDS);
    }

    /**
     * 解锁
     *
     * @param lockInfo
     * @return 是否释放成功
     */
    @Override
    public void unlock(LockInfo<InterProcessLock> lockInfo) {

        try {
            lockInfo.getLockInstance().release();
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }
}
