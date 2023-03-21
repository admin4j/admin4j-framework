package com.admin4j.framework.web.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对 CommonResult 的封装工厂类
 *
 * @author andanyang
 * @since 2022/8/4 10:55
 */
@Data
@ApiModel(value = "统一响应消息报文")
public class R<T> extends CommonResult<T> {

    /**
     * 成功
     */
    protected static final int SUCCESS = ResponseEnum.SUCCESS.getCode();

    /**
     * 失败
     */
    protected static final int FAIL = ResponseEnum.ERROR.getCode();

    /**
     * 认证失败 401
     */
    public static final int FAIL_UNAUTHORIZED = ResponseEnum.ERROR.getCode();

    static <T> R<T> restResult(T data, int code, String msg) {
        return restResult(data, code, msg, null);
    }

    /**
     * 返回 restful 格式的数据
     *
     * @param data
     * @param code
     * @param msg
     * @param i18nArgs
     * @param <T>
     * @return
     */
    static <T> R<T> restResult(T data, int code, String msg, Object[] i18nArgs) {


        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setI18nArgs(i18nArgs);
        return apiResult;
    }

    /**
     * 返回成功
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, ResponseEnum.SUCCESS.getMsg());
    }

    /**
     * 返回成功
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> okMsg(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, ResponseEnum.SUCCESS.getMsg());
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    /**
     * 返回失败
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> fail() {
        return restResult(null, FAIL, ResponseEnum.ERROR.getMsg());
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }


    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, ResponseEnum.ERROR.getMsg());
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }


    public static <T> R<T> fail(int code, String msg, Object[] i18nArgs) {
        return restResult(null, code, msg, i18nArgs);
    }


    public static <T> R<T> fail(ResponseEnum responseEnum) {
        return fail(responseEnum.getCode(), responseEnum.getMsg());
    }

    public static <T> R<T> fail(ResponseEnum responseEnum, Object[] i18nArgs) {
        return R.fail(responseEnum.getCode(), responseEnum.getMsg(), i18nArgs);
    }

    public static <T> R<T> fail(ResponseEnum responseEnum, String msg) {
        return fail(responseEnum.getCode(), msg);
    }

    /**
     * 服务降级
     *
     * @param throwable
     * @return
     */
    public static <T> R<T> fallback(Throwable throwable) {
        return restResult(null, ResponseEnum.SERVICE_FALLBACK.getCode(), throwable.getMessage());
    }


    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public boolean isOk() {
        return getCode() == R.SUCCESS;
    }

}
