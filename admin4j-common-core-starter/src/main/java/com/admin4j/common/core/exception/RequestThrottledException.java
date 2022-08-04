package com.admin4j.common.core.exception;

import com.admin4j.common.core.api.ResponseEnum;

/**
 * 请求api限制
 *
 * @author andanyang
 * @since 2022/2/8 15:27
 */
public class RequestThrottledException extends BaseException {
    public RequestThrottledException(String message) {
        super(ResponseEnum.REQUEST_THROTTLED_EXCEPTION, message);
    }
}
