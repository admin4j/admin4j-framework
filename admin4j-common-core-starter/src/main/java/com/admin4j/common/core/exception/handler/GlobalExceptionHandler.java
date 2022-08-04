package com.admin4j.common.core.exception.handler;

import com.admin4j.common.core.api.R;
import com.admin4j.common.core.api.ResponseEnum;
import com.admin4j.common.core.event.GlobalExceptionEvent;
import com.admin4j.common.core.exception.BaseException;
import com.admin4j.common.core.exception.BusinessException;
import com.admin4j.common.core.exception.PreAuthorizeException;
import com.admin4j.common.core.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局异常处理器
 *
 * @author andanyang
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public void handlerException(Exception e) {

        SpringUtils.publishEvent(new GlobalExceptionEvent(e));
    }

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<R> baseException(BaseException e) {
        log.error("基础异常:" + e.getMessage(), e);
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(e.getMessage()));
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R> baseException(BusinessException e) {
        log.error("业务异常:" + e.getMessage(), e);
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<R> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException: getMaxUploadSize:{} getMessage: {}", e.getMaxUploadSize(), e.getMessage());
        long maxUploadSize = e.getMaxUploadSize() / 1024L / 1024L;
        Object[] objects = {maxUploadSize};
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResponseEnum.MAX_UPLOAD_SIZE_EXCEPTION, objects));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<R> handleException(Exception e) {
        log.error(e.getMessage(), e);
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(e.getMessage()));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<R> validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResponseEnum.VALIDATED_BIND_EXCEPTION, message));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R> validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResponseEnum.METHOD_ARGUMENT_NOTVALID_EXCEPTION,
                e.getBindingResult().getFieldError().getField() + message));
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(PreAuthorizeException.class)
    public ResponseEntity<R> preAuthorizeException(PreAuthorizeException e) {
        log.error("权限异常：" + e.getMessage(), e);
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResponseEnum.FAIL_UNAUTHORIZED, "没有权限，请联系管理员授权"));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<R> nullPointerException(NullPointerException e) {
        log.error("空指针错误：" + e.getMessage(), e);
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResponseEnum.SERVICE_FAIL_NULL));
    }
}
