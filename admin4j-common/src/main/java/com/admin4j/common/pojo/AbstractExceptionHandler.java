package com.admin4j.common.pojo;

import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.common.util.SpringUtils;

/**
 * @author andanyang
 * @since 2023/3/13 14:51
 */
public abstract class AbstractExceptionHandler {

    public void handlerException(Exception e) {

        SpringUtils.getApplicationContext().publishEvent(new GlobalExceptionEvent(e));
    }
}
