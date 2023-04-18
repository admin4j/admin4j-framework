package com.admin4j.framework.zookeeper.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/4/18 11:31
 */
@Data
@ConfigurationProperties(prefix = "admin4j.zookeeper")
public class ZookeeperProperties {

    /**
     * zk的server地址，多个server之间使用英⽂逗号分隔开
     */
    private String connectString;

    /**
     * 隔离命名空间
     */
    private String namespace;
    /**
     * 连接超时时间， 默认是15000毫秒
     */
    private int connectionTimeoutMs = 15000;
    /**
     * 会话超时时间， 默认是60000毫秒
     */
    private int sessionTimeoutMs = 60000;
    /**
     * 初始的sleep时间，用于计算之后的每次重试的sleep时间, 默认 1000 毫秒
     */
    private int baseSleepTimeMs = 1000;
    /**
     * 最⼤重试次数
     */
    private int maxRetries = 3;

}
