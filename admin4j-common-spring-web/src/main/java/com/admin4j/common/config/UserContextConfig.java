package com.admin4j.common.config;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.util.UserContextUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author andanyang
 * @since 2023/6/7 8:54
 */
public class UserContextConfig implements InitializingBean {


    @Autowired(required = false)
    IUserContextHolder userContextHolder;


    @Override
    public void afterPropertiesSet() throws Exception {

        UserContextUtil.userContextHolder = userContextHolder;
    }
}
