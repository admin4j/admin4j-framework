package com.admin4j.framework.excel.configuration;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.alibaba.excel.exception.ExcelDataConvertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author andanyang
 * @since 2024/1/4 15:50
 */
@RestControllerAdvice
@Slf4j
public class ExcelExceptionHandler {


    @ExceptionHandler(ExcelDataConvertException.class)
    public ResponseEntity<IResponse> distributedLockException(ExcelDataConvertException e) {

        log.error("ExcelDataConvertException：" + e.getMessage());

        String msg = "ExcelDataConvertException row:" + e.getRowIndex() + " column:" + e.getColumnIndex() + " error message:" + e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SimpleResponse.of(ResponseEnum.VERIFY_ERROR.getCode(), msg));
    }

}
