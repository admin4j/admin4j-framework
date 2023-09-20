package com.admin4j.framework.feign;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;

/**
 * 用于服务直接上传文件
 *
 * @author andanyang
 * @since 2022/7/27 17:03
 */
public class MultipartSupportConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
}
