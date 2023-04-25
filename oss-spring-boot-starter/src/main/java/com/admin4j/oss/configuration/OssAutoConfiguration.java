package com.admin4j.oss.configuration;

import com.admin4j.oss.AmazonS3Factory;
import com.admin4j.oss.OssProperties;
import com.admin4j.oss.OssTemplate;
import com.admin4j.oss.UploadFileService;
import com.admin4j.oss.impl.OssTemplateImpl;
import com.admin4j.oss.impl.SimpleOSSUploadFileService;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author andanyang
 * @since 2023/4/13 15:31
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "enable", prefix = "admin4j.oss")
public class OssAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        AmazonS3 amazonS3 = AmazonS3Factory.create(ossProperties);
        return new OssTemplateImpl(amazonS3, ossProperties);
    }

    @Bean
    @ConditionalOnClass(MultipartFile.class)
    @ConditionalOnMissingBean(UploadFileService.class)
    @ConditionalOnBean(OssTemplate.class)
    public UploadFileService uploadFileService(OssTemplate ossTemplate, OssProperties ossProperties) {

        return new SimpleOSSUploadFileService(ossTemplate, ossProperties);
    }
}
