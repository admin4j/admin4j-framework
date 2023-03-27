package com.admin4j.framework.lock.config;

import com.admin4j.common.exception.handler.AbstractExceptionHandler;
import com.admin4j.common.pojo.CommonResult;
import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.framework.lock.exception.DistributedLockException;
import com.admin4j.framework.lock.exception.IdempotentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(CommonResult.class)
public class DLockGlobalExceptionHandler extends AbstractExceptionHandler {


    /**
     * 捕捉DistributedLockException 全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(DistributedLockException.class)
    public ResponseEntity<IResponse> distributedLockException(DistributedLockException e) {
        log.error("distributedLockException：" + e.getMessage(), e);

        return renderException(e, CommonResult.of(ResponseEnum.ERROR_D_LOCK.getCode(), e.getMessage()));

    }

    @ExceptionHandler(IdempotentException.class)
    public ResponseEntity<IResponse> idempotentException(IdempotentException e) {
        log.error("idempotentException：" + e.getMessage(), e);
        return renderException(e, CommonResult.of(ResponseEnum.ERROR_D_IDEMPOTENT.getCode(), e.getMessage()));
    }
}
