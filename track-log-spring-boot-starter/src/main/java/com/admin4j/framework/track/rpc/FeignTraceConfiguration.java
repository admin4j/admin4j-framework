package com.admin4j.framework.track.rpc;

import com.admin4j.framework.track.TraceLogProperties;
import com.admin4j.framework.track.TraceLogService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

/**
 * feign拦截器，传递traceId
 *
 * @author andanyang
 * @since 2024/5/7 16:22
 */
@RequiredArgsConstructor
public class FeignTraceConfiguration {

    private final TraceLogProperties traceLogProperties;

    private final TraceLogService traceLogService;

    @Bean
    public RequestInterceptor feignTraceInterceptor() {
        return template -> {


            //传递日志traceId
            String traceId = traceLogService.getTraceId();
            if (traceId != null && !traceId.isEmpty()) {
                template.header(traceLogProperties.getHeaderTraceId(), traceId);
                template.header(traceLogProperties.getHeaderSpanId(), traceLogService.getNextSpanId());
            }

        };
    }

}
