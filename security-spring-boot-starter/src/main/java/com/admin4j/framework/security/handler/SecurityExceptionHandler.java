package com.admin4j.framework.security.handler;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author andanyang
 * @since 2023/6/2 16:05
 */
@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @Deprecated
    public ResponseEntity<IResponse> handleException(Exception e) {
        log.error("AccessDeniedException：" + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SimpleResponse.of(ResponseEnum.FAIL_AUTH_FORBIDDEN.getCode(), e.getMessage()));
    }

}
