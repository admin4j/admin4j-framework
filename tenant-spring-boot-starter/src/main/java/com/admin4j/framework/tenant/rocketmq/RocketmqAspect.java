package com.admin4j.framework.tenant.rocketmq;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.tenant.TenantUserFactory;
import com.admin4j.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author andanyang
 * @since 2024/1/8 15:50
 */
@Slf4j
@Aspect
public class RocketmqAspect {

    // private final IUserContextHolder userContextHolder;

    @Around("execution(public void org.apache.rocketmq.spring.core.RocketMQListener.onMessage(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object proceed = null;
        try {


            // 设置 登录信息
            AuthenticationUser authenticationUser = TenantUserFactory.mqUser(0L);

            UserContextUtil.setUser(authenticationUser);
            proceed = point.proceed();
        } catch (Exception e) {

            log.error("mq error: {} Signature {}", e.getMessage(), point.getSignature().toShortString(), e);
            SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent("RocketMQ error", e));
            throw e;
        } finally {
            UserContextUtil.clear();
        }

        return proceed;
    }
}
