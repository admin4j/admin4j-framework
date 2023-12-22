package com.admin4j.framework.feign.exception.handler;

import com.admin4j.common.exception.handler.AbstractExceptionHandler;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.framework.feign.exception.FallbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author andanyang
 * @since 2023/12/22 9:45
 */
@Slf4j
@ConditionalOnClass(name = "com.admin4j.common.pojo.SimpleResponse")
public class FeignFallbackGlobalExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(FallbackException.class)
    public ResponseEntity<SimpleResponse> fallbackException(FallbackException e) {
        log.error("FallbackException:" + e.getMessage(), e);
        publishGlobalExceptionEvent(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SimpleResponse.of(5102, e.getMessage()));
    }


}
