package com.admin4j.lock.config;

import com.admin4j.common.pojo.AbstractExceptionHandler;
import com.admin4j.lock.exception.DistributedLockException;
import com.admin4j.web.pojo.R;
import com.admin4j.web.pojo.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author andanyang
 */
@RestControllerAdvice
@Slf4j
public class DLockGlobalExceptionHandler extends AbstractExceptionHandler {


    /**
     * 捕捉DistributedLockException 全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(DistributedLockException.class)
    @ConditionalOnClass(R.class)
    public ResponseEntity<R> distributedLockException(DistributedLockException e) {
        log.error("distributedLockException：" + e.getMessage(), e);
        handlerException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResponseEnum.ERROR_D_LOCK, e.getMessage()));
    }
}
