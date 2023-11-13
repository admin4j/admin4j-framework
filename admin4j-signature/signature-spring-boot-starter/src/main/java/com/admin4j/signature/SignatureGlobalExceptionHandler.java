package com.admin4j.signature;

import com.admin4j.common.exception.handler.AbstractExceptionHandler;
import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.framework.signature.exception.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhougang
 * @since 2023/11/10 17:58
 */
@RestControllerAdvice
@Slf4j
public class SignatureGlobalExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<IResponse> distributedLockException(SignatureException e) {
        log.error("SignatureException：" + e.getMessage(), e);

        return renderException(e, SimpleResponse.of(ResponseEnum.REQUEST_TOO_MANY_REQUESTS.getCode(), e.getMessage()));
    }

    @Override
    public ResponseEntity<IResponse> renderException(Exception e, IResponse response) {
        publishGlobalExceptionEvent(e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }
}
