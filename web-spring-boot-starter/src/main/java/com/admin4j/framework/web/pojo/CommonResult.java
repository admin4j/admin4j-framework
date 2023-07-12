package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.SimpleResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author andanyang
 * @since 2023/7/12 15:23
 */
public class CommonResult<T> extends SimpleResponse {

    @Getter
    @Setter
    protected T data;
}
