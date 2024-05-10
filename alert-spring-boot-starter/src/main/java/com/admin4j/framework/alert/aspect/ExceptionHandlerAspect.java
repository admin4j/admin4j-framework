package com.admin4j.framework.alert.aspect;

import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author andanyang
 * @since 2024/5/8 10:12
 */
@Slf4j
@Aspect
public class ExceptionHandlerAspect {

    @Around("@within(exceptionHandler) || @annotation(exceptionHandler)")
    public Object around(ProceedingJoinPoint joinPoint, ExceptionHandler exceptionHandler) throws Throwable {


        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Throwable) {
                //发送异常事件
                SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent("GlobalException", (Throwable) arg));
            }
        }
        return joinPoint.proceed();
    }
}
