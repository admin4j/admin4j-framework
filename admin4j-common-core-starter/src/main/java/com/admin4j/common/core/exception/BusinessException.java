package com.admin4j.common.core.exception;

import com.admin4j.common.core.api.IResponse;
import com.admin4j.common.core.api.ResponseEnum;

/**
 * <p>业务异常</p>
 * <p>业务处理时，出现异常，可以抛出该异常</p>
 *
 * @author andyoung
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(Exception e) {
        super(e);
    }

    public BusinessException(String message) {
        super(ResponseEnum.BIZ_ERROR, message);
    }

    public BusinessException(IResponse response, Object[] args, String message) {
        super(response, args, message);
    }

    public BusinessException(IResponse response, Object[] args, String message, Throwable cause) {
        super(response, args, message, cause);
    }
}