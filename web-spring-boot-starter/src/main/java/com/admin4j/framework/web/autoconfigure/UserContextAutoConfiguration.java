package com.admin4j.framework.web.autoconfigure;

import com.admin4j.common.constant.WebConstant;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.framework.web.SimpleUserContextHolder;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author andanyang
 * @since 2023/9/15 9:19
 */
@Configuration
@AutoConfigureOrder(WebConstant.IUserContextHolderOrder + 2)
public class UserContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IUserContextHolder.class)
    @ConditionalOnClass(name = "com.alibaba.ttl.TransmittableThreadLocal")
    public IUserContextHolder userContextHolder() {
        return new SimpleUserContextHolder();
    }
}
