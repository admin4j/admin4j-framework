package com.admin4j.oss;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/4/13 15:18
 */
@Data
@ConfigurationProperties(prefix = "admin4j.oss")
public class OssProperties implements InitializingBean {
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
     * oss访问链接默认有效期 秒s;
     * -1 没有有效期；视为public类型的桶，否则为private私有桶
     */
    private int expires = -1;

    /**
     * 最大线程数，默认： 100
     */
    private Integer maxConnections = 100;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * 初始化方法
     * - Endpoint 不能带 /
     * - PreviewUrl, IntranetUrl 需要带 /
     */
    public void init() {

        if (StringUtils.endsWith(getEndpoint(), "/")) {
            setEndpoint(StringUtils.substring(getEndpoint(), 0, -1));
        }

        if (!StringUtils.endsWith(getPreviewUrl(), "/")) {
            setPreviewUrl(getPreviewUrl() + "/");
        }
        if (!StringUtils.endsWith(getIntranetUrl(), "/")) {
            setIntranetUrl(getIntranetUrl() + "/");
        }
    }
}
