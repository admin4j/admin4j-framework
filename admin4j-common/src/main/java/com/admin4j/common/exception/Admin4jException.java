package com.admin4j.common.exception;


import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础异常
 *
 * @author andanyang
 */
@Data
public class Admin4jException extends RuntimeException implements IResponse {
    private static final long serialVersionUID = 1L;

    /**
     * 异常消息参数
     */
    protected Object[] args;


    private int code;

    private String msg;


    public Admin4jException(Throwable throwable) {
        this(throwable.getMessage(), throwable);
    }


    public Admin4jException(String message, Throwable throwable) {
        super(message, throwable);
        setMsg(message);
        setCode(ResponseEnum.ERROR_E.getCode());
    }

    public Admin4jException(String message, Throwable throwable, Object... args) {
        super(message, throwable);
        setMsg(message);
        setCode(ResponseEnum.ERROR_E.getCode());
        this.args = args;
    }

    public Admin4jException(IResponse response) {
        super(response.getMsg());
        setResponse(response);
    }

    public Admin4jException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Admin4jException(String message, Object... args) {
        this(ResponseEnum.ERROR, message);
        this.args = args;
    }

    public Admin4jException(IResponse response, String message, Object... args) {
        this(response, message, null, args);
    }


    public Admin4jException(IResponse response, String message, Throwable cause, Object... args) {
        super(StringUtils.defaultString(message, response.getMsg()), cause);
        setResponse(response);
        if (StringUtils.isNotBlank(message)) {
            setMsg(message);
        }
        this.args = args;
    }

    @SneakyThrows
    public static void throwException(Throwable exception) {
        throw exception;
    }

    @Override
    public String getMsg() {

        return StringUtils.isEmpty(msg) ? super.getMessage() : msg;
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
}
