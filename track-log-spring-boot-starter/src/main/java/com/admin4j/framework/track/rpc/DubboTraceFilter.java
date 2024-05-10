package com.admin4j.framework.track.rpc;

import com.admin4j.framework.track.TraceLogProperties;
import com.admin4j.framework.track.TraceLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Dubbo拦截器，传递traceId
 *
 * @author andanyang
 * @since 2024/5/7 17:53
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -1)
@RequiredArgsConstructor
public class DubboTraceFilter implements Filter {
    private final TraceLogProperties traceLogProperties;
    private final TraceLogService traceLogService;

    /**
     * 服务消费者：传递traceId给下游服务
     * 服务提供者：获取traceId并赋值给MDC
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isProviderSide = RpcContext.getContext().isProviderSide();
        if (isProviderSide) {
            //服务提供者逻辑
            String traceId = invocation.getAttachment(traceLogProperties.getHeaderTraceId());
            String spanId = invocation.getAttachment(traceLogProperties.getHeaderSpanId());
            traceLogService.initWithProviderTrace(traceId, spanId);
        } else {
            //服务消费者逻辑
            String traceId = traceLogService.getTraceId();
            if (StringUtils.isNotEmpty(traceId)) {
                invocation.setAttachment(traceLogProperties.getHeaderTraceId(), traceId);
                invocation.setAttachment(traceLogProperties.getHeaderSpanId(), traceLogService.getNextSpanId());
            }
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            if (isProviderSide) {
                traceLogService.removeTrace();
            }
        }
    }

}
