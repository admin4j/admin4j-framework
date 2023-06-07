package com.admin4j.common.config;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.spring.util.SpringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author andanyang
 * @since 2023/6/7 8:54
 */
public class UserContextConfig implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        UserContextUtil.userContextHolder = SpringUtils.getBean(IUserContextHolder.class);
    }
}
