package com.admin4j.framework.track.impl;

import com.admin4j.framework.track.TraceLogProperties;
import com.admin4j.framework.track.TraceLogService;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author andanyang
 * @since 2024/5/7 16:24
 */
@AllArgsConstructor
public class SimpleTraceLogService implements TraceLogService {

    private static final TransmittableThreadLocal<AtomicInteger> SPAN_NUMBER = new TransmittableThreadLocal<>();
    private final TraceLogProperties traceLogProperties;

    /**
     * 创建traceId
     */
    public String createTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String getNextSpanId() {
        int i = SPAN_NUMBER.get().incrementAndGet();
        return getSpanId() + "." + i;
    }

    protected void initSpanNumber() {
        SPAN_NUMBER.set(new AtomicInteger());
    }


    /**
     * 创建traceId并赋值MDC
     */
    @Override
    public void initWithProviderTrace(String traceId, String spanId) {
        if (StringUtils.isBlank(traceId)) {
            traceId = createTraceId();
            spanId = "0";
        }

        MDC.put(traceLogProperties.getTraceId(), traceId);
        MDC.put(traceLogProperties.getSpanId(), spanId);
        initSpanNumber();
    }

    @Override
    public String getTraceId() {
        return MDC.get(traceLogProperties.getTraceId());
    }

    @Override
    public String getSpanId() {
        return StringUtils.defaultString(MDC.get(traceLogProperties.getSpanId()), "0");
    }

    @Override
    public void removeTrace() {

        MDC.remove(traceLogProperties.getTraceId());
        MDC.remove(traceLogProperties.getSpanId());
    }
}
