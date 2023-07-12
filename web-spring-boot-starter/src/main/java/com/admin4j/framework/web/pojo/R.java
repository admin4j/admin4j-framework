package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对 SimpleResponse 的封装工厂类
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
    protected static final int FAIL_RESPONSE = ResponseEnum.ERROR.getCode();

    /**
     * 认证失败 401
     */
    public static final int FAIL_UNAUTHORIZED = ResponseEnum.ERROR.getCode();

    private static final R OK = restResult(null, SUCCESS, ResponseEnum.SUCCESS.getMsg());
    private static final R FAIL = restResult(null, FAIL_RESPONSE, ResponseEnum.ERROR.getMsg());

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
        return (R<T>) OK;
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
        return (R<T>) FAIL;
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL_RESPONSE, msg);
    }


    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL_RESPONSE, ResponseEnum.ERROR.getMsg());
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL_RESPONSE, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }


    public static <T> R<T> fail(int code, String msg, Object[] i18nArgs) {
        return restResult(null, code, msg, i18nArgs);
    }


    public static <T> R<T> fail(IResponse response) {
        return fail(response.getCode(), response.getMsg());
    }

    public static <T> R<T> fail(IResponse response, Object[] i18nArgs) {
        return R.fail(response.getCode(), response.getMsg(), i18nArgs);
    }

    public static <T> R<T> fail(IResponse response, String msg) {
        return fail(response.getCode(), msg);
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
