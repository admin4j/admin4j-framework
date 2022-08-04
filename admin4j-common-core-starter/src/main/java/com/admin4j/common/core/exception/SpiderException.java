package com.admin4j.common.core.exception;

import com.admin4j.common.core.api.ResponseEnum;

/**
 * 爬虫错误
 *
 * @author andanyang
 * @since 2021/12/16 9:36
 */
public class SpiderException extends BaseException {
    public SpiderException(String message) {
        super(message);
        setResponse(ResponseEnum.ERROR_SPIDER);
    }

    public SpiderException(Exception exception) {
        super(exception);
        setResponse(ResponseEnum.ERROR_SPIDER);
    }
}
