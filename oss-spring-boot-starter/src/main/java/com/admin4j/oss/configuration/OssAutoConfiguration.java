package com.admin4j.oss.configuration;

import com.admin4j.oss.OssProperties;
import com.admin4j.oss.OssTemplate;
import com.admin4j.oss.UploadFileService;
import com.admin4j.oss.impl.OssTemplateImpl;
import com.admin4j.oss.impl.SimpleOSSUploadFileService;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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
    @ConditionalOnMissingBean(AmazonS3.class)
    public AmazonS3 ossClient(OssProperties ossProperties) {
        // 客户端配置，主要是全局的配置信息
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(ossProperties.getMaxConnections());
        // url以及region配置
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                ossProperties.getEndpoint(), ossProperties.getRegion());
        // 凭证配置
        AWSCredentials awsCredentials = new BasicAWSCredentials(ossProperties.getAccessKey(),
                ossProperties.getSecretKey());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        // build amazonS3Client客户端
        return AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration).withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding().withPathStyleAccessEnabled(ossProperties.getPathStyleAccess()).build();
    }

    @Bean
    @ConditionalOnBean(AmazonS3.class)
    public OssTemplate ossTemplate(AmazonS3 amazonS3, OssProperties ossProperties) {
        return new OssTemplateImpl(amazonS3, ossProperties);
    }

    @Bean
    @ConditionalOnMissingBean(UploadFileService.class)
    @ConditionalOnClass(MultipartFile.class)
    @ConditionalOnBean(OssTemplate.class)
    public UploadFileService uploadFileService(OssTemplate ossTemplate, OssProperties ossProperties) {

        return new SimpleOSSUploadFileService(ossTemplate, ossProperties);
    }
}
