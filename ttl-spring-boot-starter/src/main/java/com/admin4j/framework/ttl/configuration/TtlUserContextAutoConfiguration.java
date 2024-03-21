package com.admin4j.framework.ttl.configuration;

import com.admin4j.common.constant.WebConstant;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.service.impl.TtlUserContextHolder;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * @author andanyang
 * @since 2024/3/5 16:16
 */
@AutoConfigureOrder(WebConstant.IUserContextHolderOrder - 1)
@ConditionalOnMissingBean(IUserContextHolder.class)
public class TtlUserContextAutoConfiguration {

    @Bean
    @Lazy
    public IUserContextHolder ttlUserContextHolder() {
        return new TtlUserContextHolder();
    }
}
