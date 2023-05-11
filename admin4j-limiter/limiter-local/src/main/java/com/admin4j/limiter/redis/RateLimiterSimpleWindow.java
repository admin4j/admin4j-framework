//package com.admin4j.limiter;
//
//import com.admin4j.limiter.constant.LimiterType;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author andanyang
// * @since 2023/5/11 10:14
// */
//public class RateLimiterSimpleWindow implements RateLimiterProvider {
//
//    // 计数器
//    private static AtomicInteger REQ_COUNT = new AtomicInteger();
//    private static long START_TIME = System.currentTimeMillis();
//
//
//    @Override
//    public boolean support(LimiterType limiterType) {
//        return LimiterType.SLIDING_WINDOW.equals(limiterType);
//    }
//
//    /**
//     * 判断请求是否允许通过
//     *
//     * @param key
//     * @param qps
//     * @param time
//     * @return
//     */
//    @Override
//    public synchronized boolean tryAcquire(String key, int qps, int time) {
//
//
//        if ((System.currentTimeMillis() - START_TIME) > time) {
//            REQ_COUNT.set(0);
//            START_TIME = System.currentTimeMillis();
//        }
//        return REQ_COUNT.incrementAndGet() <= qps;
//    }
//
//
//}
