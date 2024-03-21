package com.admin4j.framework.lock.aspect;


import com.admin4j.framework.lock.LockInfo;
import com.admin4j.framework.lock.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 分布式锁解析器
 * https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
 *
 * @author andanyang
 * @since 2020/12/22 11:06
 */
@Slf4j
@Aspect
public class DistributedLockAspect extends AbstractDLockHandler {

    @Around("@within(distributedLock) || @annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {

        if (distributedLock == null) {
            distributedLock = joinPoint.getTarget().getClass().getAnnotation(DistributedLock.class);
        }

        // 获取锁信息
        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockModel(distributedLock.lockModel());
        lockInfo.setLockKey(getDistributedLockKey(joinPoint, distributedLock));
        lockInfo.setTryLock(distributedLock.tryLock());
        lockInfo.setLeaseTime(distributedLock.leaseTime());
        lockInfo.setWaitTimeOutSeconds(distributedLock.waitTimeOutSeconds());
        lockInfo.setTenant(distributedLock.tenant());
        lockInfo.setUser(distributedLock.user());
        lockInfo.setExecutor(distributedLock.executor());

        return super.around(joinPoint, lockInfo);
    }
}
