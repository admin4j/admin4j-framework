package com.admin4j.framework.track;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author andanyang
 * @since 2024/5/6 18:49
 */

public class MDCTaskDecorator implements TaskDecorator {

    /**
     * Decorate the given {@code Runnable}, returning a potentially wrapped
     * {@code Runnable} for actual execution, internally delegating to the
     * original {@link Runnable#run()} implementation.
     *
     * @param runnable the original {@code Runnable}
     * @return the decorated {@code Runnable}
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        
        // 获取当前线程的 MDC 上下文信息
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                // 设置新的执行线程的 MDC 上下文信息
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                // 执行任务
                runnable.run();
            } finally {
                // 清除 MDC 上下文信息
                MDC.clear();
            }
        };
    }
}
