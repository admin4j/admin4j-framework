package com.admin4j.elasticsearch.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/4/18 11:31
 */
@Data
@ConfigurationProperties(prefix = "admin4j.elasticsearc")

public class ElasticsearchProperties {

    /**
     * es的server地址，多个server之间使用英⽂逗号分隔开
     */
    private String urls;

    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;

 
}
