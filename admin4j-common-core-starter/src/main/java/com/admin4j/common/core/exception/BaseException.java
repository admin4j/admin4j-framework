package com.admin4j.common.core.exception;

import com.admin4j.common.core.api.IResponse;
import com.admin4j.common.core.api.ResponseEnum;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础异常
 *
 * @author andanyang
 */
@Data
public class BaseException extends RuntimeException implements IResponse {
    private static final long serialVersionUID = 1L;

    /**
     * 异常消息参数
     */
    protected Object[] args;


    private int code;

    private String msg;


    @Override
    public String getMsg() {

        return StringUtils.isEmpty(msg) ? super.getMessage() : msg;
    }


    public BaseException(Throwable throwable) {
        this(throwable.getMessage(), throwable);
    }

    public BaseException(String message, Throwable throwable) {
        super(throwable);
        setMsg(message);
        setCode(ResponseEnum.ERROR_E.getCode());
    }

    public BaseException(IResponse response) {
        super(response.getMsg());
        setResponse(response);
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(String message) {
        this(ResponseEnum.ERROR, message);
    }

    public BaseException(IResponse response, String message) {
        this(response, null, message);
    }

    public BaseException(IResponse response, Object[] args, String message) {
        super(StringUtils.defaultString(message, response.getMsg()));
        setResponse(response);
        this.args = args;
    }

    public BaseException(IResponse response, Object[] args, String message, Throwable cause) {
        super(StringUtils.defaultString(message, response.getMsg()), cause);
        setResponse(response);
        this.args = args;
    }

    protected void setResponse(IResponse response) {
        this.msg = response.getMsg();
        this.code = response.getCode();
    }

    /**
     * 抛出异常
     */
    public void throwException() {
        throw this;
    }

    @SneakyThrows
    public static void throwException(Throwable exception) {
        throw exception;
    }
}
