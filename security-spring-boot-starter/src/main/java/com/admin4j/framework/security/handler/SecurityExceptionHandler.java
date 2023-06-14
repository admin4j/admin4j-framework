package com.admin4j.framework.security.handler;

import com.admin4j.common.exception.handler.AbstractExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author andanyang
 * @since 2023/6/2 16:05
 */
@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler extends AbstractExceptionHandler {

//    @ExceptionHandler(SignatureException.class)
//    @Deprecated
//    public ResponseEntity<IResponse> handleException(Exception e) {
//        log.error("SignatureException：" + e.getMessage(), e);
//        return renderException(e, SimpleResponse.of(ResponseEnum.FAIL_AUTH_TOKEN_ERROR.getCode(), e.getMessage()));
//    }

}
