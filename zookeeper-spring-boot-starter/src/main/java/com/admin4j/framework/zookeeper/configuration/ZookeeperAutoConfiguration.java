package com.admin4j.framework.zookeeper.configuration;

import com.admin4j.framework.zookeeper.properties.ZookeeperProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/4/18 11:29
 */
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework zkClient(ZookeeperProperties zookeeperProperties) {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(), zookeeperProperties.getMaxRetries());
        return CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getConnectString())
                .namespace(zookeeperProperties.getNamespace())
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
                .retryPolicy(retryPolicy)
                .build();
    }

}
