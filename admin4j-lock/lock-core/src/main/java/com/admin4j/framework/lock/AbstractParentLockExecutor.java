package com.admin4j.framework.lock;

import lombok.Getter;
import lombok.Setter;

/**
 * @author andanyang
 * @since 2024/1/16 17:04
 */
public abstract class AbstractParentLockExecutor<T> implements LockExecutor<T> {

    @Getter
    @Setter
    protected LockExecutor<?> parent;

    @Override
    public void setLockInstance(LockInfo lockInfo) {

        if (parent != null) {
            parent.setLockInstance(lockInfo);
            lockInfo.setParentLockInstance(lockInfo.getLockInstance());
            lockInfo.setLockInstance(null);
        }
        T lockInstanceSelf = getLockInstanceSelf(lockInfo);
        lockInfo.setLockInstance(lockInstanceSelf);
    }

    protected abstract T getLockInstanceSelf(LockInfo lockInfo);

    @Override
    public void lock(LockInfo lockInfo) {

        if (parent != null) {

            Object lockInstance = lockInfo.getLockInstance();
            lockInfo.setLockInstance(lockInfo.getParentLockInstance());
            parent.lock(lockInfo);
            lockInfo.setLockInstance(lockInstance);
        }

        lockSelf(lockInfo);
    }

    /**
     * 实现自身的锁
     *
     * @param lockInfo
     */
    protected abstract void lockSelf(LockInfo lockInfo);

    @Override
    public boolean tryLock(LockInfo lockInfo) {

        if (parent != null) {
            // 先获取本地锁
            Object lockInstance = lockInfo.getLockInstance();
            lockInfo.setLockInstance(lockInfo.getParentLockInstance());
            if (!parent.tryLock(lockInfo)) {
                // 未获取本地锁直接返回
                lockInfo.setLockInstance(lockInstance);
                return false;
            }
            lockInfo.setLockInstance(lockInstance);
        }

        return tryLockSelf(lockInfo);
    }

    protected abstract boolean tryLockSelf(LockInfo lockInfo);

    @Override
    public void unlock(LockInfo lockInfo) {

        if (parent != null) {

            Object lockInstance = lockInfo.getLockInstance();
            lockInfo.setLockInstance(lockInfo.getParentLockInstance());
            // 先解锁本地锁
            parent.unlock(lockInfo);
            lockInfo.setLockInstance(lockInstance);
        }

        unlockSelf(lockInfo);
    }

    protected abstract void unlockSelf(LockInfo lockInfo);
}
