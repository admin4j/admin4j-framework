package com.admin4j.common.exception.handler;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.spring.util.SpringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author andanyang
 * @since 2023/3/13 14:51
 */
@RestControllerAdvice
public abstract class AbstractExceptionHandler {


    public void publishGlobalExceptionEvent(Exception e) {
        SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent("GlobalException", e));
    }

    public ResponseEntity<IResponse> renderException(Exception e, IResponse response) {
        publishGlobalExceptionEvent(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
