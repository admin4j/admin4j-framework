package com.admin4j.common.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author andanyang
 * @since 2022/8/4 10:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> implements IResponse {
    protected int code;

    protected String msg;

    protected T data;

    /**
     * 多语言参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected Object[] i18nArgs;

    protected void setResponse(IResponse response) {
        code = response.getCode();
        msg = response.getMsg();
    }

    public CommonResult(IResponse response) {
        setResponse(response);
    }

    public static <T> CommonResult<T> of(int code, String msg) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMsg(msg);
        return commonResult;
    }
}
