package com.admin4j.framework.track;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2024/5/7 16:08
 */
@Data
@ConfigurationProperties(prefix = "admin4j.trace-log")
public class TraceLogProperties {

    /**
     * 追踪id的名称
     */
    private String traceId = "traceId";
    /**
     * 块id的名称
     */
    private String spanId = "spanId";
    /**
     * 日志链路追踪id信息头
     */
    private String headerTraceId = "x-traceId";
    /**
     * 日志链路块id信息头
     */
    private String headerSpanId = "x-spanId";
    /**
     * 是否开启日志追踪
     */
    private boolean enable = true;


}
