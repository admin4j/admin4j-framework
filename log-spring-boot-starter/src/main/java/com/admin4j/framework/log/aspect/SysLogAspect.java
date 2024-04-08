package com.admin4j.framework.log.aspect;

import com.admin4j.framework.log.ISysLogService;
import com.admin4j.framework.log.annotation.SysLog;
import com.admin4j.framework.log.event.SysLogEvent;
import com.admin4j.spring.util.SpelUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @author andanyang
 * @since 2023/6/14 13:42
 */
@Aspect
@Slf4j
public class SysLogAspect {

    @Autowired
    ISysLogService sysLogService;

    @Around("@annotation(sysLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {

        String strClassName = point.getTarget().getClass().getName();
        Signature signature = point.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return point.proceed();
        }

        String strMethodName = signature.getName();
        log.debug("SysLog[类名]:{},[方法]:{}", strClassName, strMethodName);


        SysLogEvent sysLogEvent = generateEvent(point, sysLog);

        Long startTime = System.currentTimeMillis();
        Object obj;
        try {
            obj = point.proceed();
        } catch (Exception e) {

            sysLogEvent.setException(StringUtils.substring(e.getMessage(), 0, 44));
            throw e;
        } finally {
            Long endTime = System.currentTimeMillis();
            sysLogEvent.setDuration(endTime - startTime);
            // 只负责发送事件，不负责消费事件，具体时间由使用者实现
            sysLogService.saveLog(sysLogEvent);
        }

        return obj;
    }

    public SysLogEvent generateEvent(ProceedingJoinPoint point, SysLog sysLog) {

        String content = sysLog.content();
        String type = sysLog.type();
        String[] args = sysLog.args();
        
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = SpelUtil.parse(point.getTarget(), args[i], method, point.getArgs());
            }
        }

        if (StringUtils.isBlank(content)) {
            content = sysLog.value();
            return sysLogService.generateEvent(type, content, args);
        }


        content = SpelUtil.parse(point.getTarget(), content, method, point.getArgs());


        return sysLogService.generateEvent(type, content, args);
    }

}
