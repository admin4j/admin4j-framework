package com.admin4j.framework.lock;

import com.admin4j.framework.lock.constant.LockModel;
import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.framework.lock.pool.PooledLockFactory;
import com.admin4j.framework.lock.pool.WrapperLockObject;
import com.admin4j.framework.lock.pool.WrapperReadWriteLockObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.time.Duration;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author andanyang
 * @since 2024/1/16 15:48
 */
@Slf4j
public class LocalLockExecutor implements LockExecutor<WrapperLockObject> {

    private static final Map<String, WrapperLockObject> LOCK_MAP = new ConcurrentHashMap<>(32);
    private static KeyedObjectPool<LockModel, WrapperLockObject> LOCK_POOL;

    // private GenericKeyedObjectPoolConfig<WrapperLockObject> config;

    public LocalLockExecutor() {
        this(defaultPoolInit());
    }

    public LocalLockExecutor(GenericKeyedObjectPoolConfig<WrapperLockObject> config) {
        LOCK_POOL = new GenericKeyedObjectPool<>(new PooledLockFactory(), config);
    }

    protected static GenericKeyedObjectPoolConfig<WrapperLockObject> defaultPoolInit() {
        // 2. 给池子添加支持的配置信息
        GenericKeyedObjectPoolConfig<WrapperLockObject> config = new GenericKeyedObjectPoolConfig<>();
        // 2.1 最大池化对象数量
        config.setMaxTotal(-1);
        config.setMaxTotalPerKey(-1);
        // 2.2 最大空闲池化对象数量
        config.setMaxIdlePerKey(20);
        // 2.3 最小空闲池化对象数量
        config.setMinIdlePerKey(8);
        // 2.4 间隔多久检查一次池化对象状态,驱逐空闲对象,检查最小空闲数量小于就创建
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(-1));
        // 2.5 阻塞就报错
        config.setBlockWhenExhausted(true);
        // 2.6 最大等待时长超过5秒就报错,如果不配置一直进行等待
        config.setMaxWait(Duration.ofMinutes(1));
        // 2.7 是否开启jmx监控,默认开启
        config.setJmxEnabled(true);
        // 2.8 一定要符合命名规则,否则无效
        config.setJmxNameBase("org.apache.commons.pool2:type=LocalLockPool,name=LocalLock");
        // 生成数据库连接池
        // 连接池配置最大5个连接setMaxTotal(5),但是获取6次,那么有一次获取不到就会阻塞setBlockWhenExhausted(true),
        // 当等待了10秒setMaxWait(Duration.ofSeconds(10))还是获取不到。就直接报错
        return config;
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

        WrapperLockObject lock = (WrapperLockObject) lockInfo.getLockInstance();
        lock.unlock();

        // lock 无效
        if (lock.decrement() < 0) {

            LOCK_MAP.remove(lockInfo.getLockKey());
            try {
                LOCK_POOL.returnObject(lockInfo.getLockModel(), lock);
            } catch (Exception e) {
                log.error("Lock returnObject failed: {}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        log.debug("local UnLock success {}", lockInfo.getLockKey());
    }

    /**
     * 根据锁信息获取锁
     *
     * @param lockInfo
     * @return 获取锁实例
     */
    @Override
    public void initSetLockInstance(LockInfo lockInfo) {

        if (lockInfo.getLockInstance() != null) {
            return;
        }

        WrapperLockObject lock = getLockObject(lockInfo);

        lockInfo.setLockInstance(lock);
    }


    WrapperLockObject getLockObject(LockInfo lockInfo) {

        return LOCK_MAP.compute(lockInfo.getLockKey(), (key, value) -> {

            try {
                WrapperLockObject wrapperLockObject;
                if (value == null) {
                    wrapperLockObject = borrowObject(lockInfo);
                } else if (!value.isActive()) {
                    wrapperLockObject = borrowObject(lockInfo);
                } else {
                    wrapperLockObject = value;
                }
                wrapperLockObject.increment();

                return wrapperLockObject;
            } catch (NoSuchElementException e) {
                throw new DistributedLockException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected WrapperLockObject borrowObject(LockInfo lockInfo) throws Exception {

        WrapperLockObject wrapperLockObject;
        switch (lockInfo.getLockModel()) {
            case READ:
                wrapperLockObject = LOCK_POOL.borrowObject(LockModel.READ);
                ((WrapperReadWriteLockObject) wrapperLockObject).setRead(true);
                break;
            case WRITE:
                wrapperLockObject = LOCK_POOL.borrowObject(LockModel.READ);
                ((WrapperReadWriteLockObject) wrapperLockObject).setRead(false);
                break;
            default:
                wrapperLockObject = LOCK_POOL.borrowObject(lockInfo.getLockModel());
        }

        return wrapperLockObject;
    }
}
