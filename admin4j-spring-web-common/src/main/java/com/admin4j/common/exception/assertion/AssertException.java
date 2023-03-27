package com.admin4j.common.exception.assertion;

import com.admin4j.common.exception.BizException;
import com.admin4j.common.pojo.IResponse;

/**
 * @author andanyang
 * @since 2022/3/21 13:54
 */
public class AssertException extends BizException {


    public AssertException(IResponse response, Throwable throwable) {
        super(throwable);
        super.setResponse(response);
    }

    public AssertException(IResponse response, Throwable throwable, String msg) {
        super(throwable);
        super.setResponse(response);
        super.setMsg(msg);
    }

    public AssertException(IResponse response, String msg) {
        super(response, null, msg);
    }

}
