package com.admin4j.lock.aspect;


import com.admin4j.lock.annotation.DistributedLock;
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
public class DistributedLockHandler extends AbstractDLockHandler {

    @Around("@annotation(distributedLock)")
    @Override
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        return super.around(joinPoint, distributedLock);
    }
}
