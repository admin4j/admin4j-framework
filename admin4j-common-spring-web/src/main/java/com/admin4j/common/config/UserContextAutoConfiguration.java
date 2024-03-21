package com.admin4j.common.config;

import com.admin4j.common.constant.WebConstant;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.service.impl.SimpleUserContextHolder;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author andanyang
 * @since 2023/9/15 9:19
 */
@Configuration
@AutoConfigureOrder(WebConstant.IUserContextHolderOrder)
public class UserContextAutoConfiguration {


    @Bean
    @Lazy
    @ConditionalOnMissingBean(IUserContextHolder.class)
    public IUserContextHolder userContextHolder() {
        return new SimpleUserContextHolder();
    }
}
