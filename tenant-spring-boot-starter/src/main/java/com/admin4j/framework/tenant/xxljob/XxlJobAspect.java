package com.admin4j.framework.tenant.xxljob;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.tenant.TenantUserFactory;
import com.admin4j.spring.util.SpringUtils;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author andanyang
 * @since 2023/9/20 16:31
 */
@Slf4j
@Aspect
public class XxlJobAspect {


    @Around("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object proceed = null;
        try {

            //设置 登录信息
            AuthenticationUser authenticationUser = TenantUserFactory.jobUser(0L);
            UserContextUtil.setUser(authenticationUser);

            long beginTime = System.currentTimeMillis();
            log.debug("XXL-JOB:{} beginTime {}", XxlJobHelper.getJobId(), beginTime);
            proceed = point.proceed();

            long endTime = System.currentTimeMillis();
            log.debug("XXL-JOB:{} success endTime {}", XxlJobHelper.getJobId(), endTime);
            //XxlJobHelper.handleSuccess(TraceContext.traceId());
        } catch (Exception e) {
            log.error("XXL-JOB error: {}", e.getMessage(), e);
            SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent("XXL-JOB", e));

            long endTime = System.currentTimeMillis();
            log.debug("XXL-JOB:{} fail endTime {}", XxlJobHelper.getJobId(), endTime);
            throw e;
        } finally {

            UserContextUtil.clear();
        }

        return proceed;
    }
}
