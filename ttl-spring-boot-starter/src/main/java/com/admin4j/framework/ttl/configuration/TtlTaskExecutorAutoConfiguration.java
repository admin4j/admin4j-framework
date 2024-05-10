package com.admin4j.framework.ttl.configuration;

import com.admin4j.framework.ttl.props.ThreadPoolProperties;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author andanyang
 * @since 2022/11/4 16:00
 */
// TODO 参考 TaskExecutionAutoConfiguration 写一个
@AutoConfigureBefore(TaskExecutionAutoConfiguration.class)
@EnableConfigurationProperties(ThreadPoolProperties.class)
@Slf4j
public class TtlTaskExecutorAutoConfiguration {
    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    /**
     * @return
     */
    @Bean(name = {TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME,
            AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME})
    @Primary
    public Executor taskExecutor(@Autowired(required = false) List<TaskDecorator> taskDecorators) {
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        /*
           rejection-policy：当pool已经达到max size的时候，如何处理新任务
           CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setDaemon(true);
        // 没有设置下面参数，在kill -15时，线程池没有执行结束，会被强制关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationMillis());
        //executor.setThreadFactory();
        executor.setTaskDecorator(taskDecorator(taskDecorators));
        executor.initialize();
        return TtlExecutors.getTtlExecutor(executor);
    }

    protected TaskDecorator taskDecorator(List<TaskDecorator> taskDecorators) {
        if (taskDecorators != null && !taskDecorators.isEmpty()) {
            if (taskDecorators.size() == 1) {
                return taskDecorators.get(0);
            }
            return (Runnable runnable) -> {

                for (TaskDecorator taskDecorator : taskDecorators) {
                    runnable = taskDecorator.decorate(runnable);
                }
                return runnable;
            };
        }
        return null;
    }
}
