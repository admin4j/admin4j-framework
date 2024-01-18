package com.admin4j.framework.lock.util;


import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.LockInfo;
import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @author andanyang
 * @since 2021/12/23 10:33
 */
@Slf4j
public class DistributedLockUtil {

    /**
     * redis key 前缀
     */
    public static final String DISTRIBUTED_LOCK_PRE = "D_LOCK:";

    private static LockExecutor<?> DEFAULT_LOCK_EXECUTOR;

    /**
     * 设置默认执行器
     *
     * @param lockExecutor 默认执行器
     */
    public static void setDefaultLockExecutor(LockExecutor<?> lockExecutor) {
        DistributedLockUtil.DEFAULT_LOCK_EXECUTOR = lockExecutor;
    }

    /**
     * 更新 lockInfo 获取锁执行器
     *
     * @param lockInfo 加锁信息
     * @return 锁执行器
     */
    public static LockExecutor<?> getLockExecutor(LockInfo lockInfo) {

        if (lockInfo.getExecutor() == null || lockInfo.getExecutor() == LockExecutor.class) {
            return DEFAULT_LOCK_EXECUTOR;
        } else {
            return SpringUtils.getBean(lockInfo.getExecutor());
        }
    }

    /**
     * 加锁，block直到解锁
     *
     * @param lockKey  lockKey
     * @param supplier 执行 supplier
     * @param <T>      返回类型
     * @return 返回值
     */
    public static <T> T lock(String lockKey, Supplier<T> supplier) {

        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockKey(lockKey);
        return lock(lockInfo, supplier);
    }


    /**
     * 加锁，block直到解锁。
     *
     * @param lockInfo 锁信息
     * @param supplier 运行方法
     * @param <T>      supplier 返回类型
     * @return supplier 返回值
     */
    public static <T> T lock(LockInfo lockInfo, Supplier<T> supplier) {

        LockExecutor<?> lockExecutor = getLockExecutor(lockInfo);
        lockExecutor.initSetLockInstance(lockInfo);

        try {
            lockExecutor.lock(lockInfo);
            return supplier.get();
        } finally {
            lockExecutor.unlock(lockInfo);
        }
    }

    /**
     * 加锁，block直到解锁。无返回值
     *
     * @param lockKey  lockKey
     * @param runnable 运行
     */
    public static void lock(String lockKey, Runnable runnable) {

        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockKey(lockKey);

        DEFAULT_LOCK_EXECUTOR.initSetLockInstance(lockInfo);

        try {
            DEFAULT_LOCK_EXECUTOR.lock(lockInfo);
            runnable.run();
        } finally {
            DEFAULT_LOCK_EXECUTOR.unlock(lockInfo);
        }
    }

    /**
     * 尝试获取锁。不会block
     *
     * @param lockKey  lockKey
     * @param runnable runnable
     * @return 获取锁是否成功
     */
    public static boolean tryLock(String lockKey, Runnable runnable) {

        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockKey(lockKey);
        lockInfo.setTryLock(true);

        DEFAULT_LOCK_EXECUTOR.initSetLockInstance(lockInfo);

        try {
            if (!DEFAULT_LOCK_EXECUTOR.tryLock(lockInfo)) {
                log.debug("DistributedLockUtil tryLock fail {}", lockKey);
                return false;
            } else {
                runnable.run();
            }
        } finally {
            DEFAULT_LOCK_EXECUTOR.unlock(lockInfo);
        }

        return true;
    }


    /**
     * 尝试获取锁。不会block。
     * 有返回值
     *
     * @param lockKey  lockKey
     * @param supplier supplier
     * @return 获取锁是否成功
     */
    public static <T> T tryLock(String lockKey, Supplier<T> supplier) {

        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockKey(lockKey);
        lockInfo.setTryLock(true);
        DEFAULT_LOCK_EXECUTOR.initSetLockInstance(lockInfo);

        try {
            if (!DEFAULT_LOCK_EXECUTOR.tryLock(lockInfo)) {
                log.debug("DistributedLockUtil tryLock fail");
                return null;
            } else {
                return supplier.get();
            }
        } finally {
            DEFAULT_LOCK_EXECUTOR.unlock(lockInfo);
        }
    }

    /**
     * 尝试获取锁。不会block。
     * 无返回值
     * 获取失败会抛出错误
     *
     * @param lockKey  lockKey
     * @param runnable runnable
     */
    public static void tryLockWithError(String lockKey, Runnable runnable) {
        if (!tryLock(lockKey, runnable)) {
            throw new DistributedLockException("DistributedLock tryLock fail");
        }
    }


    /**
     * 尝试获取锁。不会block。
     * 有返回值
     * 获取失败会抛出错误
     *
     * @param lockKey  lockKey
     * @param supplier supplier
     */
    public static <T> T tryLockWithError(String lockKey, Supplier<T> supplier) {
        T t = tryLock(lockKey, supplier);
        if (t == null) {
            throw new DistributedLockException("DistributedLock tryLock fail");
        }
        return t;
    }
}
