package com.admin4j.lock.util;

import com.admin4j.common.util.SpringUtils;
import com.admin4j.lock.exception.DistributedLockException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
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

    private static RedissonClient redissonClient;

    public static <T> T lock(String lockName, Supplier<T> supplier) {

        RedissonClient redisson = getRedissonClient();
        RLock lock = redisson.getLock(DistributedLockUtil.DISTRIBUTED_LOCK_PRE + lockName);
        lock.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    public static void lock(String lockName, Runnable runnable) {

        RedissonClient redisson = getRedissonClient();
        RLock lock = redisson.getLock(DistributedLockUtil.DISTRIBUTED_LOCK_PRE + lockName);
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    public static boolean tryLock(String lockName, Runnable runnable) {

        RedissonClient redisson = getRedissonClient();
        RLock lock = redisson.getLock(DistributedLockUtil.DISTRIBUTED_LOCK_PRE + lockName);

        try {
            if (!lock.tryLock(0, TimeUnit.MICROSECONDS)) {
                log.error("DistributedLockUtil tryLock fail {}", lockName);
                return false;

            } else {
                try {
                    runnable.run();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.error("DistributedLockUtil tryLock error:" + e.getMessage(), e);
            throw new DistributedLockException(e);
        }
        return true;
    }

    public static <T> T tryLock(String lockName, Supplier<T> supplier) {

        RedissonClient redisson = getRedissonClient();
        RLock lock = redisson.getLock(DistributedLockUtil.DISTRIBUTED_LOCK_PRE + lockName);

        try {
            if (!lock.tryLock(0, TimeUnit.MICROSECONDS)) {
                log.debug("DistributedLockUtil tryLock fail");
                return null;
            } else {
                try {
                    return supplier.get();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.error("DistributedLockUtil tryLock error:" + e.getMessage(), e);
            throw new DistributedLockException(e);
        }
    }

    public static void tryLockWithError(String lockName, Runnable runnable) {
        if (!tryLock(lockName, runnable)) {
            throw new DistributedLockException("DistributedLock tryLock fail");
        }
    }


    public static <T> T tryLockWithError(String lockName, Supplier<T> supplier) {
        T t = tryLock(lockName, supplier);
        if (t == null) {
            throw new DistributedLockException("DistributedLock tryLock fail");
        }
        return t;
    }


    private static RedissonClient getRedissonClient() {
        if (redissonClient == null) {
            redissonClient = SpringUtils.getBean(RedissonClient.class);
        }
        return redissonClient;
    }
}
