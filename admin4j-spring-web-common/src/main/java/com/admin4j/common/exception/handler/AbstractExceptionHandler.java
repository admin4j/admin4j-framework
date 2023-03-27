package com.admin4j.common.exception.handler;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.common.util.SpringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author andanyang
 * @since 2023/3/13 14:51
 */
public abstract class AbstractExceptionHandler {

    @Deprecated
    public void handlerException(Exception e) {

        SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent("GlobalException", e));
    }

    public ResponseEntity<IResponse> renderException(Exception e, IResponse response) {
        SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent("GlobalException", e));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
