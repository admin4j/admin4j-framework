package com.admin4j.framework.lock.aspect;


import com.admin4j.common.service.ILoginUserInfoService;
import com.admin4j.framework.lock.LockInfo;
import com.admin4j.framework.lock.annotation.Idempotent;
import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.framework.lock.exception.IdempotentException;
import com.admin4j.spring.util.SpelUtil;
import com.admin4j.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 分布式锁解析器
 * https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
 *
 * @author andanyang
 * @since 2020/12/22 11:06
 */
@Slf4j
@Aspect
public class IdempotentAspect extends AbstractDLockHandler {

    /**
     * 切面环绕通知
     *
     * @param joinPoint  ProceedingJoinPoint
     * @param idempotent idempotent
     * @return Object
     */
    @Around("@within(idempotent) ||@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {

        if (idempotent == null) {
            idempotent = joinPoint.getTarget().getClass().getAnnotation(Idempotent.class);
        }

        //获取锁信息
        LockInfo<Object> lockInfo = new LockInfo<>();
        lockInfo.setLockModel(idempotent.lockModel());
        lockInfo.setLockKey(getIdempotentLockKey(joinPoint, idempotent));
        lockInfo.setTryLock(idempotent.tryLock());
        lockInfo.setLeaseTime(idempotent.leaseTime());
        lockInfo.setWaitTimeOutSeconds(idempotent.waitTimeOutSeconds());
        lockInfo.setTenant(false);
        lockInfo.setUser(true);
        lockInfo.setExecutor(idempotent.executor());

        return super.around(joinPoint, lockInfo);
    }

    @Override
    protected void lockFailure() {
        throw new IdempotentException("failed to acquire Idempotent");
    }


    protected String getIdempotentLockKey(ProceedingJoinPoint joinPoint, Idempotent idempotent) {

        //得到被切面修饰的方法的参数列表
        Object[] args = joinPoint.getArgs();
        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        StringBuilder distributedLockKey = new StringBuilder(idempotent.prefix());

        String key = StringUtils.defaultString(idempotent.key(), idempotent.value());
        if (StringUtils.isEmpty(key)) {
            //按照 KeyGenerator 生成key
            key = generateKeyByKeyGenerator(joinPoint, idempotent.keyGenerator());
            distributedLockKey.append(key);
        } else {
            //按照 key 生成key
            String parseElKey = SpelUtil.parse(joinPoint.getTarget(), key, method, args);

            if (StringUtils.isBlank(parseElKey)) {
                log.error("DistributedLockKey is null Signature: {}", joinPoint.getSignature());
                throw new DistributedLockException("DistributedLockKey is null");
            }

            distributedLockKey.append(parseElKey);
        }

        //开启用户模式
        ILoginUserInfoService loginUserService = SpringUtils.getBean(ILoginUserInfoService.class);
        Assert.notNull(loginUserService, "ILoginUserInfoService must implement");
        distributedLockKey.append(":U").append(loginUserService.getUserId());

        return distributedLockKey.toString();
    }
}
