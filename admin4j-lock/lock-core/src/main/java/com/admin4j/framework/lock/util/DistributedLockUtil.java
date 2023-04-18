package com.admin4j.framework.lock.util;

import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.LockInfo;
import com.admin4j.framework.lock.exception.DistributedLockException;
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

    private static LockExecutor lockExecutor;

    public static void setLockExecutor(LockExecutor lockExecutor) {
        DistributedLockUtil.lockExecutor = lockExecutor;
    }

    public static <T> T lock(String lockName, Supplier<T> supplier) {

        LockInfo<Object> lockInfo = new LockInfo<>();
        lockInfo.setLockKey(lockName);
        return lock(lockInfo, supplier);
    }

    public static <T> T lock(LockInfo lockInfo, Supplier<T> supplier) {

        Object lock = lockExecutor.getLock(lockInfo);
        lockInfo.setLockInstance(lock);

        lockExecutor.lock(lockInfo);
        try {
            return supplier.get();
        } finally {
            lockExecutor.unlock(lockInfo);
        }
    }

    public static void lock(String lockName, Runnable runnable) {

        LockInfo<Object> lockInfo = new LockInfo<>();
        lockInfo.setLockKey(lockName);
        Object lock = lockExecutor.getLock(lockInfo);
        lockInfo.setLockInstance(lock);

        lockExecutor.lock(lockInfo);

        try {
            runnable.run();
        } finally {
            lockExecutor.unlock(lockInfo);
        }
    }

    public static boolean tryLock(String lockName, Runnable runnable) {

        LockInfo<Object> lockInfo = new LockInfo<>();
        lockInfo.setLockKey(lockName);
        lockInfo.setTryLock(true);
        Object lock = lockExecutor.getLock(lockInfo);
        lockInfo.setLockInstance(lock);


        if (!lockExecutor.tryLock(lockInfo)) {
            log.error("DistributedLockUtil tryLock fail {}", lockName);
            return false;

        } else {
            try {
                runnable.run();
            } finally {
                lockExecutor.unlock(lockInfo);
            }
        }

        return true;
    }

    public static <T> T tryLock(String lockName, Supplier<T> supplier) {

        LockInfo<Object> lockInfo = new LockInfo<>();
        lockInfo.setLockKey(lockName);
        lockInfo.setTryLock(true);
        Object lock = lockExecutor.getLock(lockInfo);
        lockInfo.setLockInstance(lock);


        if (!lockExecutor.tryLock(lockInfo)) {
            log.debug("DistributedLockUtil tryLock fail");
            return null;
        } else {
            try {
                return supplier.get();
            } finally {
                lockExecutor.unlock(lockInfo);
            }
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
}
