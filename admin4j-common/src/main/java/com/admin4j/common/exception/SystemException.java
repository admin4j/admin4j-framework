package com.admin4j.common.exception;


import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;

/**
 * 用户操作业务时，提示系统程序的异常信息，这类的异常信息时用户看不懂的，需要告警通知程序员排查对应的问题，
 * 如 NullPointerException，IndexOfException。
 * 另一个情况就是接口对接时，参数的校验时提示出来的信息，
 * 如：缺少ID，缺少必须的参数等，
 * 这类的信息对于客户来说也是看不懂的，也是解决不了的，
 * 所以我把这两类的错误应当统一归类于系统异常
 *
 * @author andanyang
 * @since 2022/3/21 13:42
 */
public class SystemException extends Admin4jException {
    private static final long serialVersionUID = 1L;

    public SystemException(Exception e) {
        super(e);
    }

    public SystemException(String message) {
        super(ResponseEnum.BIZ_ERROR, message);
    }

    public SystemException(String message, Throwable throwable) {
        super(ResponseEnum.BIZ_ERROR, message, throwable);
    }

    public SystemException(IResponse response, String message, Object... args) {
        super(response, message, args);
    }

    public SystemException(IResponse response, String message, Throwable cause, Object... args) {
        super(response, message, cause, args);
    }
}
