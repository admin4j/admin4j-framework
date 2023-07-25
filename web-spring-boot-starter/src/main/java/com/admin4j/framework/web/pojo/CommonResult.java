package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.IResponse;
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
}
