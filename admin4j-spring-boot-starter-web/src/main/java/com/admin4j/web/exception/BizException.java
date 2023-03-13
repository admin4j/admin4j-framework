package com.admin4j.web.exception;

import com.admin4j.web.pojo.IResponse;
import com.admin4j.web.pojo.ResponseEnum;

/**
 * <p>业务异常</p>
 * <p>业务处理时，出现异常，可以抛出该异常</p>
 *
 * @author andyoung
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(Exception e) {
        super(e);
    }

    public BizException(String message) {
        super(ResponseEnum.BIZ_ERROR, message);
    }

    public BizException(IResponse response, Object[] args, String message) {
        super(response, args, message);
    }

    public BizException(IResponse response, Object[] args, String message, Throwable cause) {
        super(response, args, message, cause);
    }
}