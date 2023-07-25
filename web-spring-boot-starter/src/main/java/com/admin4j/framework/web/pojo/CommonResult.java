package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.IResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @author andanyang
 * @since 2023/7/12 15:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> implements IResponse {

    protected int code;

    protected String msg;

    @Getter
    @Setter
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
}
