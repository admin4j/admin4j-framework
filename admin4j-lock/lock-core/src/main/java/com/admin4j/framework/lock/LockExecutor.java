package com.admin4j.framework.lock;

/**
 * 分布式锁核心处理器
 *
 * @author andanyang
 * @since 2023/4/18 8:52
 */
public interface LockExecutor<T> {

    /**
     * 根据锁信息获取锁
     *
     * @param lockInfo
     * @return 获取锁实例
     */
    T getLock(LockInfo<T> lockInfo);

    /**
     * 加锁,获取到锁会block，直到解锁
     *
     * @param lockInfo 锁信息
     */
    void lock(LockInfo<T> lockInfo);

    /**
     * 尝试获取锁，不会阻塞
     *
     * @param lockInfo
     * @return true 获取成功，false 获取锁失败
     */
    boolean tryLock(LockInfo<T> lockInfo);

    /**
     * 解锁
     */
    void unlock(LockInfo<T> lockInfo);
}
