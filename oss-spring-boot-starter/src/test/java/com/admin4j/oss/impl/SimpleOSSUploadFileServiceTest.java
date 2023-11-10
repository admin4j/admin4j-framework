package com.admin4j.oss.impl;

import com.admin4j.oss.AmazonS3Factory;
import com.admin4j.oss.OssProperties;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author andanyang
 * @since 2023/11/10 9:48
 */
class SimpleOSSUploadFileServiceTest {

    static SimpleOSSUploadFileService simpleOSSUploadFileService;

    @BeforeAll
    public static void init() {

        OssProperties ossProperties = new OssProperties();
        ossProperties.setEndpoint("http://192.168.1.13:9901/");
        ossProperties.setAccessKey("4NdsQG6g6hzJfwko");
        ossProperties.setSecretKey("zeSSPz3WC3Wn4zBZsYtxK9YXhZ7Hjnnv");
        ossProperties.setBucket("crm-email");
        ossProperties.setPreviewUrl("http://192.168.1.13:9902/crm-email");
        ossProperties.setIntranetUrl("http://192.168.1.13:9902/crm/");
        ossProperties.setExpires(300);

        ossProperties.init();

        AmazonS3 amazonS3 = AmazonS3Factory.create(ossProperties);
        OssTemplateImpl ossTemplate = new OssTemplateImpl(amazonS3, ossProperties);

        simpleOSSUploadFileService = new SimpleOSSUploadFileService(ossTemplate, ossProperties);
    }

    @Test
    void getPreviewUrl() {

        String previewUrl = simpleOSSUploadFileService.getPreviewUrl("attachment/20231005/cc56268d64944a7348d43741129292fa.jpg");
        System.out.println("previewUrl = " + previewUrl);
    }

    @Test
    void getPrivateUrl() {
    }

    @Test
    void testGetPrivateUrl() {
    }

    @Test
    void getPreviewIntranetUrl() {
    }
}