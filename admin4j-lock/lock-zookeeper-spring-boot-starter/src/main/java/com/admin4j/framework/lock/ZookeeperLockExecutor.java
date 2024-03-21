package com.admin4j.framework.lock;

import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.framework.lock.exception.UnSupportException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ZookeeperLockExecutor extends AbstractParentLockExecutor<InterProcessLock> {

    private final CuratorFramework curatorFramework;

    /**
     * 根据锁信息获取锁
     *
     * @param lockInfo
     * @return 获取锁实例
     */
    @Override
    public InterProcessLock getLockInstanceSelf(LockInfo lockInfo) {
        InterProcessReadWriteLock interProcessReadWriteLock;
        String lockKey = lockInfo.getLockKey();
        if (!StringUtils.startsWith(lockKey, "/")) {
            lockKey = "/" + lockKey;
        }
        switch (lockInfo.getLockModel()) {
            case FAIR:
                // 公平锁
                throw new UnSupportException("Zookeeper Not supported FAIR Lock");
            case READ:
                interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework, lockKey);
                return interProcessReadWriteLock.readLock();
            case WRITE:
                // 写之前加写锁，写锁加锁成功，读锁只能等待
                interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework, lockKey);
                return interProcessReadWriteLock.writeLock();
            case REENTRANT:
            default:
                // 可重入锁
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
    protected void lockSelf(LockInfo lockInfo) {
        ((InterProcessLock) lockInfo.getLockInstance()).acquire();
        log.debug("zookeeper Lock success {}", lockInfo.getLockKey());
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
        boolean acquire = ((InterProcessLock) lockInfo.getLockInstance()).acquire(lockInfo.getWaitTimeOutSeconds(), TimeUnit.SECONDS);
        if (acquire) {
            log.debug("zookeeper tryLock success {}", lockInfo.getLockKey());
        } else {
            log.debug("zookeeper tryLock failed {}", lockInfo.getLockKey());
        }
        return acquire;
    }

    /**
     * 解锁
     *
     * @param lockInfo
     * @return 是否释放成功
     */
    @Override
    protected void unlockSelf(LockInfo lockInfo) {

        try {
            ((InterProcessLock) lockInfo.getLockInstance()).release();

            log.debug("zookeeper UnLock success {}", lockInfo.getLockKey());
        } catch (Exception e) {
            log.debug("zookeeper UnLock fail {}", lockInfo.getLockKey());
            throw new DistributedLockException(e);
        }
    }
}
