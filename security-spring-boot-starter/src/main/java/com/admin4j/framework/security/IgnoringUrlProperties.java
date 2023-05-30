package com.admin4j.framework.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 忽略url 配置文件
 *
 * @author andanyang
 * @since 2023/3/24 17:00
 */
@Data
@ConfigurationProperties(prefix = "admin4j.security.ignoring")
public class IgnoringUrlProperties {

    /**
     * 包含所有请求类型的路径
     */
    private String[] uris;
    /**
     * get 请求
     */
    private String[] get;
    /**
     * post 请求
     */
    private String[] post;
    /**
     * put 请求
     */
    private String[] put;
    /**
     * delete 请求
     */
    private String[] delete;
    /**
     * patch 请求
     */
    private String[] patch;


}
