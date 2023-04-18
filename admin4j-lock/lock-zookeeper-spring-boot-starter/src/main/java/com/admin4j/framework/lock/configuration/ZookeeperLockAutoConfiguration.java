package com.admin4j.framework.lock.configuration;

import com.admin4j.framework.lock.ZookeeperLockExecutor;
import com.admin4j.framework.zookeeper.configuration.ZookeeperAutoConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/4/18 11:29
 */
@AutoConfigureAfter(ZookeeperAutoConfiguration.class)
public class ZookeeperLockAutoConfiguration {

    @Bean
    @ConditionalOnBean(CuratorFramework.class)
    public ZookeeperLockExecutor zookeeperLockExecutor(CuratorFramework curatorFramework) {
        return new ZookeeperLockExecutor(curatorFramework);
    }
}
