package com.admin4j.limiter;

import com.admin4j.common.exception.handler.AbstractExceptionHandler;
import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.limiter.core.exception.RateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author andanyang
 * @since 2023/5/11 17:58
 */
@RestControllerAdvice
@Slf4j
public class LimiterGlobalExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(RateLimiterException.class)
    public ResponseEntity<IResponse> distributedLockException(RateLimiterException e) {
        log.error("RateLimiterException：" + e.getMessage(), e);

        return renderException(e, SimpleResponse.of(ResponseEnum.REQUEST_TOO_MANY_REQUESTS.getCode(), e.getMessage()));
    }

    @Override
    public ResponseEntity<IResponse> renderException(Exception e, IResponse response) {
        publishGlobalExceptionEvent(e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }
}
