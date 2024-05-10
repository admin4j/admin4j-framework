package com.admin4j.framework.track.configuration;

import com.admin4j.framework.track.MDCTaskDecorator;
import com.admin4j.framework.track.TraceLogProperties;
import com.admin4j.framework.track.TraceLogService;
import com.admin4j.framework.track.impl.SimpleTraceLogService;
import com.admin4j.framework.track.rpc.FeignTraceConfiguration;
import com.admin4j.framework.track.rpc.WebTrackLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2024/5/6 15:15
 */
@EnableConfigurationProperties(TraceLogProperties.class)
@ConditionalOnProperty(value = "admin4j.trace-log", matchIfMissing = true)
public class TrackLogAutoConfiguration {

    @Bean
    public TraceLogService traceLogService(TraceLogProperties properties) {
        return new SimpleTraceLogService(properties);
    }

    @Bean
    public WebTrackLogFilter trackLogFilter(TraceLogProperties properties, TraceLogService traceLogService) {
        return new WebTrackLogFilter(properties, traceLogService);
    }

    @Bean
    public MDCTaskDecorator mdcTaskDecorator() {
        return new MDCTaskDecorator();
    }

    @Bean
    @ConditionalOnClass(name = {"feign.RequestInterceptor"})
    public FeignTraceConfiguration feignTraceConfiguration(TraceLogProperties traceLogProperties, TraceLogService traceLogService) {
        return new FeignTraceConfiguration(traceLogProperties, traceLogService);
    }

    //@Bean
    //@ConditionalOnClass(name = {"org.apache.dubbo.rpc.Filter"})
    //public DubboTraceFilter dubboTraceFilter(TraceLogProperties traceLogProperties, TraceLogService traceLogService) {
    //    return new DubboTraceFilter(traceLogProperties, traceLogService);
    //}
}
