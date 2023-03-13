package com.admin4j.web.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author andanyang
 * @since 2022/8/4 10:52
 */
@Data
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
}
