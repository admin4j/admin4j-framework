package com.admin4j.framework.ttl.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2022/11/4 16:02
 */
@ConfigurationProperties("admin4j.async")
public class ThreadPoolProperties {
    /**
     * 线程池维护线程的最小数量.
     */
    private int corePoolSize = 10;
    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize = 200;
    /**
     * 队列最大长度
     */
    private int queueCapacity = 10;
    /**
     * 线程池前缀
     */
    private String threadNamePrefix = "Admin4jExecutor-";

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}
