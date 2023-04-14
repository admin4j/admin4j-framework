package com.admin4j.framework.lock.annotation;

import com.admin4j.framework.lock.constant.LockModel;
import com.admin4j.framework.lock.key.DLockKeyGenerator;
import com.admin4j.framework.lock.util.DistributedLockUtil;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author andanyang
 * @since 2023/3/14 8:43
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DistributedLock(tryLock = true, user = true)
public @interface Idempotent {

    /**
     * @return 锁key的前缀
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "prefix")
    String prefix() default DistributedLockUtil.DISTRIBUTED_LOCK_PRE;

    /**
     * REENTRANT(可重入锁),FAIR(公平锁),MULTIPLE(联锁),REDLOCK(红锁),READ(读锁), WRITE(写锁),
     * AUTO(自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE)
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "lockModel")
    LockModel lockModel() default LockModel.REENTRANT;

    /**
     * 分布式锁名称
     *
     * @return String
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = DistributedLock.class, attribute = "key")
    String key() default "";


    /**
     * The bean name of the custom  {@link DLockKeyGenerator } to use.
     * Mutually exclusive with the key attribute.
     *
     * @return
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "keyGenerator")
    String keyGenerator() default "";

    /**
     * 是否尝试获取锁。成功获取则进入锁；获取失败则抛出异常。
     * true 这个方法无论如何都会立即返回，不会阻塞
     *
     * @return boolean
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "tryLock")
    boolean tryLock() default true;

    /**
     * 过期时间/续租时间, 未设置过期时间（-1）时则会有watchDog(30s)的锁续约
     * 设置了过期时间，则会这是redis的过期时间。再指定时间过期
     * If expireSeconds is -1, hold the lock until explicitly unlocked. 默认30s
     *
     * @return int
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "leaseTime")
    int leaseTime() default -1;

    /**
     * 最多等待x秒
     * If waitSeconds <0 , no wait TimeOut
     * waitSeconds = 0  使用 tryLock 模式，获取不到锁就就失败
     *
     * @return
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "waitTimeOutSeconds")
    int waitTimeOutSeconds() default -1;

    /**
     * 开启租户模式
     * 需要实现 ILoginUserInfoService 接口告诉当前登录用户的租户信息
     *
     * @return boolean
     */
    @AliasFor(annotation = DistributedLock.class, attribute = "tenant")
    boolean tenant() default false;
}
