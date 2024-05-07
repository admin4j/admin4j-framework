package com.admin4j.framework.track;

/**
 * @author andanyang
 * @since 2024/5/7 16:24
 */

public interface TraceLogService {


    /**
     * 获取下一个 spanId
     *
     * @return
     */
    String getNextSpanId();


    /**
     * 初始化trace 信息 如果有traceId 则使用，如果没有则自己创建
     */
    void initWithProviderTrace(String traceId, String spanId);

    /**
     * 获取 traceId
     *
     * @return
     */
    String getTraceId();

    /**
     * 获取 spanId
     *
     * @return
     */
    String getSpanId();

    /**
     * 移除 trace 信息
     */
    void removeTrace();
}
