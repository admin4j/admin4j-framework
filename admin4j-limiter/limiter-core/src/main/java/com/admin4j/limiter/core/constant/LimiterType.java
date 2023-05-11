package com.admin4j.limiter.core.constant;

/**
 * @author andanyang
 * @since 2023/5/11 9:38
 */
public enum LimiterType {
    /**
     * 固定窗口算法
     */
    FIX_WINDOW,
    /**
     * 滑动窗口
     */
    SLIDING_WINDOW,

    /**
     * 滑动日志
     */
    SLIDING_LOG,

    /**
     * 漏桶算法
     */
    LEAKY_BUCKET,

    /**
     * 令牌桶算法
     */
    TOKEN_BUCKET
}
