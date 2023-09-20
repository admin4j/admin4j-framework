package com.admin4j.framework.tenant.autoconfigure;

import com.admin4j.framework.tenant.xxljob.XxlJobAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/9/20 9:05
 */
@ConditionalOnClass(name = "com.xxl.job.core.handler.annotation.XxlJob")
public class TXxlJobAutoConfiguration {

    @Bean
    public XxlJobAspect xxlJobAspect() {
        return new XxlJobAspect();
    }
}
