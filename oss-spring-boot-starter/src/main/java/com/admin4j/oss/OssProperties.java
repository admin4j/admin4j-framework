package com.admin4j.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/4/13 15:18
 */
@Data
@ConfigurationProperties(prefix = "admin4j.oss")
public class OssProperties {
    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * 区域
     */
    private String region;

    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle模式 {http://endpoint/bucketname}
     * false supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style 模式{http://bucketname.endpoint}
     * 只是url的显示不一样
     */
    private Boolean pathStyleAccess = true;

    /**
     * Access key
     */
    private String accessKey;

    /**
     * Secret key
     */
    private String secretKey;
    /**
     * 默认的bucketName
     */
    private String bucket;
    /**
     * 预览 url（cdn url）.
     * 默认null 使用oss 预览方式
     */
    private String previewUrl;
    /*
     * 内网访问Url
     */
    private String intranetUrl;

    /**
     * 最大线程数，默认： 100
     */
    private Integer maxConnections = 100;
}
