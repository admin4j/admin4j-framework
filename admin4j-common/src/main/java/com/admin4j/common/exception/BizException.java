package com.admin4j.common.exception;


import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;

/**
 * <p>业务异常</p>
 * <p>业务处理时，出现异常，可以抛出该异常</p>
 * 比如：“用户没有登录”，“没有权限操作”。
 *
 * @author andyoung
 */
public class BizException extends Admin4jException {

    private static final long serialVersionUID = 1L;

    public BizException(Throwable e) {
        super(e);
    }

    public BizException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BizException(String message, Object... args) {
        super(ResponseEnum.BIZ_ERROR, message);
    }

    public BizException(IResponse response, String message, Object... args) {
        super(response, message, args);
    }

    public BizException(IResponse response, String message, Throwable cause, Object... args) {
        super(response, message, cause, args);
    }
}