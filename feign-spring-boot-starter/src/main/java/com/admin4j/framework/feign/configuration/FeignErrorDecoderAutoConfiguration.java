package com.admin4j.framework.feign.configuration;

import com.admin4j.framework.feign.FeignErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/12/22 9:41
 */
@ConditionalOnClass(name = "com.admin4j.json.JSONUtil")
public class FeignErrorDecoderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FeignErrorDecoder.class)
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
