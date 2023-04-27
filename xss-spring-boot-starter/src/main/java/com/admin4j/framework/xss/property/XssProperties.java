package com.admin4j.framework.xss.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/4/27 10:24
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "admin4j.xss")
public class XssProperties {


    /**
     * 匹配模式
     * 0：全部关闭(不使用xss)
     * 1 忽略模式 （按照 ignoreUris 没有出现在这里的才会执行xss过滤)
     * 2 包含模式 （按照 includeUris 只有出现在这里的才会执行xss过滤 )
     */
    private int matchPattern = 0;
    /**
     * 忽略 uri 列表
     */
    private List<String> ignoreUris;

    /**
     * 包含uri 列表
     */
    private List<String> includeUris;
    /**
     * 忽略参数
     */
    private List<String> ignoreParam;

    /**
     * 防护xss策略
     * 可选值："","ebay","anythinggoes","myspace","slashdot","tinymce"
     */
    private String antisamyPolicy = "ebay";
}
