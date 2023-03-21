package com.admin4j.framework.lock.aspect;


import com.admin4j.common.service.ILoginUserInfoService;
import com.admin4j.common.util.SpelUtil;
import com.admin4j.common.util.SpringUtils;
import com.admin4j.framework.lock.annotation.DistributedLock;
import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.framework.lock.key.DLockKeyGenerator;
import com.admin4j.framework.lock.key.SimpleKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁解析器
 * https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
 *
 * @author andanyang
 * @since 2020/12/22 11:06
 */
@Slf4j
public abstract class AbstractDLockHandler {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 切面环绕通知
     *
     * @param joinPoint       ProceedingJoinPoint
     * @param distributedLock DistributedLock
     * @return Object
     */

    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {

        distributedLock = AnnotationUtils.getAnnotation(distributedLock, DistributedLock.class);
        //获取超时时间并获取锁
        RLock lock = getLock(joinPoint, distributedLock);
        if (!distributedLock.tryLock()) {
            lock.lock(distributedLock.leaseTime(), TimeUnit.SECONDS);
        } else {
            boolean res = lock.tryLock(distributedLock.waitTimeOutSeconds(), distributedLock.leaseTime(), TimeUnit.SECONDS);
            if (!res) {
                lockFailure();
            }
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
            //log.debug("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑...");
        }
    }

    /**
     * 抢锁失败，抛出异常
     */
    protected void lockFailure() {
        throw new DistributedLockException("failed to acquire lock");
    }

    /**
     * 根据注解获取分布式锁
     *
     * @param distributedLock
     * @return
     */
    private RLock getLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {

        String lockName = getDistributedLockKey(joinPoint, distributedLock);
        switch (distributedLock.lockModel()) {
            case FAIR:
                //公平锁
                return redissonClient.getFairLock(lockName);
            case READ:
                //读之前加读锁，读锁的作用就是等待该lockkey释放写锁以后再读
                RReadWriteLock readLock = redissonClient.getReadWriteLock(lockName);
                return readLock.readLock();
            case WRITE:
                //写之前加写锁，写锁加锁成功，读锁只能等待
                RReadWriteLock writeLock = redissonClient.getReadWriteLock(lockName);
                return writeLock.writeLock();
            case REENTRANT:
            default:
                //可重入锁
                return redissonClient.getLock(lockName);
        }
    }


    private String generateKeyByKeyGenerator(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        //得到被切面修饰的方法的参数列表
        Object[] args = joinPoint.getArgs();
        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DLockKeyGenerator dLockKeyGenerator = StringUtils.isEmpty(distributedLock.keyGenerator()) ? defaultDLockKeyGenerator() : SpringUtils.getBean(DLockKeyGenerator.class);
        return dLockKeyGenerator.generate(joinPoint.getTarget(), method, args).toString();
    }

    private String getDistributedLockKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {

        //得到被切面修饰的方法的参数列表
        Object[] args = joinPoint.getArgs();
        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        StringBuilder distributedLockKey = new StringBuilder(distributedLock.prefix());

        if (StringUtils.isEmpty(distributedLock.key())) {
            //按照 KeyGenerator 生成key
            String key = generateKeyByKeyGenerator(joinPoint, distributedLock);
            distributedLockKey.append(key);
        } else {
            //按照 key 生成key
            String parseElKey = SpelUtil.parse(joinPoint.getTarget(), distributedLock.value(), method, args);
            Assert.isTrue(StringUtils.isNotEmpty(parseElKey), "DistributedLockKey is null");
            distributedLockKey.append(parseElKey);
        }

        //开启用户模式
        if (distributedLock.user()) {
            ILoginUserInfoService loginUserService = SpringUtils.getBean(ILoginUserInfoService.class);
            Assert.notNull(loginUserService, "ILoginUserInfoService must implement");
            distributedLockKey.append(":U").append(loginUserService.getUserId());
        }

        //开启租户
        if (distributedLock.tenant()) {
            ILoginUserInfoService loginUserService = SpringUtils.getBean(ILoginUserInfoService.class);
            Assert.notNull(loginUserService, "ILoginUserInfoService must implement");
            Assert.notNull(loginUserService.getTenant(), "Tenant not null");
            distributedLockKey.append(":T").append(loginUserService.getTenant());
        }

        return distributedLockKey.toString();
    }

    private static DLockKeyGenerator LOCK_KEY_GENERATOR;

    protected DLockKeyGenerator defaultDLockKeyGenerator() {

        if (LOCK_KEY_GENERATOR == null) {
            LOCK_KEY_GENERATOR = new SimpleKeyGenerator();
        }
        return LOCK_KEY_GENERATOR;
    }
}
