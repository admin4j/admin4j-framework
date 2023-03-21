package com.admin4j.framework.lock.constant;

/**
 * @author andanyang
 * @since 2021/12/22 11:27
 */
public enum LockModel {
    /**
     * REENTRANT(可重入锁),FAIR(公平锁),MULTIPLE(联锁),REDLOCK(红锁),READ(读锁), WRITE(写锁),
     * AUTO(自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE)
     */
    REENTRANT,
    /**
     * 公平锁
     * 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     * 所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，
     * 也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
     */
    FAIR,
    /**
     * 读之前加读锁，读锁的作用就是等待该lockkey释放写锁以后再读
     */
    READ,
    /**
     * 写之前加写锁，写锁加锁成功，读锁只能等待
     */
    WRITE
    /**
     * 联锁:
     * 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，
     * 每个RLock对象实例可以来自于不同的Redisson实例。
     * ```
     * RLock lock1 = redissonInstance1.getLock("lock1");
     * RLock lock2 = redissonInstance2.getLock("lock2");
     * RLock lock3 = redissonInstance3.getLock("lock3");
     * <p>
     * RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
     * // 同时加锁：lock1 lock2 lock3
     * // 所有的锁都上锁成功才算成功。
     * lock.lock();
     * ...
     * lock.unlock();
     * ```
     */
    //MULTIPLE,
    //AUTO
}
