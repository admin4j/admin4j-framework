package com.admin4j.limiter.redis;

/**
 * @author andanyang
 * @since 2023/5/11 10:26
 */
public class SimpleWindowCounter {

    private int count = 0; // 计数器
    private long startTime; // 开始时间
    private int limit; // 请求次数限制
    private long interval; // 时间间隔

    public SimpleWindowCounter(int limit, long interval) {
        this.limit = limit;
        this.interval = interval;
        this.startTime = System.currentTimeMillis();
    }

    public synchronized boolean check() {
        long now = System.currentTimeMillis();
        if (now - startTime > interval) {
            count = 0;
            startTime = now;
        }
        if (count >= limit) {
            return false;
        }
        count++;
        return true;
    }
}
