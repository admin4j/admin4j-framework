package com.admin4j.framework.tenant.autoconfigure;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.framework.tenant.openfeign.FeignRequestInterceptor;
import com.admin4j.framework.tenant.openfeign.UserContextHolderFilter;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author andanyang
 * @since 2023/9/20 9:42
 */
@ConditionalOnBean(RequestInterceptor.class)
public class TFeignAutoConfiguration {

    @Bean
    @ConditionalOnBean(IUserContextHolder.class)
    public FeignRequestInterceptor feignRequestInterceptor(IUserContextHolder userContextHolder) {
        return new FeignRequestInterceptor(userContextHolder);
    }

    @Bean
    @ConditionalOnBean(IUserContextHolder.class)
    @Order
    public UserContextHolderFilter userContextHolderFilter(IUserContextHolder userContextHolder) {
        return new UserContextHolderFilter(userContextHolder);
    }
}
