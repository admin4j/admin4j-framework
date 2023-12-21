package com.admin4j.common.service.impl;

import com.admin4j.common.service.IUserContextHolder;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 使用ttl
 * 当前登录用户上下文信息，可实现切换用户，切换租户。
 *
 * @author andanyang
 * @since 2021/7/27 10:56
 */
public class TtlUserContextHolder extends SimpleUserContextHolder implements IUserContextHolder {

    public TtlUserContextHolder() {
        /**
         * 支持父子线程之间的数据传递 THREAD_LOCAL_TENANT
         */
        super(new TransmittableThreadLocal<>());
    }

}
