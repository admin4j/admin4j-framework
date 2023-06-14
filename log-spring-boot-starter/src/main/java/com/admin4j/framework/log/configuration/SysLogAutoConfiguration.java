package com.admin4j.framework.log.configuration;

import com.admin4j.framework.log.ISysLogService;
import com.admin4j.framework.log.aspect.SysLogAspect;
import com.admin4j.framework.log.impl.SysLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/4/13 15:31
 */

public class SysLogAutoConfiguration {

    @Bean
    public SysLogAspect sysLogAspect() {

        return new SysLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean(ISysLogService.class)
    public ISysLogService sysLogService() {
        return new SysLogService();
    }
}
