package com.admin4j.framework.track.rpc;

import com.admin4j.framework.track.TraceLogProperties;
import com.admin4j.framework.track.TraceLogService;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2024/5/6 14:51
 */
@WebFilter(filterName = "TrackLog", urlPatterns = "/**")
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class WebTrackLogFilter extends OncePerRequestFilter {

    private final TraceLogProperties traceLogProperties;
    private final TraceLogService traceLogService;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = request;
        String traceId = httpServletRequest.getHeader(traceLogProperties.getHeaderTraceId());
        String spanId = httpServletRequest.getHeader(traceLogProperties.getHeaderSpanId());

        traceLogService.initWithProviderTrace(traceId, spanId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            traceLogService.removeTrace();
        }
    }
}
