package com.admin4j.framework.lock.configuration;

import com.admin4j.framework.lock.LockExecutor;
import com.admin4j.framework.lock.ZookeeperLockExecutor;
import com.admin4j.framework.zookeeper.configuration.ZookeeperAutoConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/4/18 11:29
 */
@AutoConfigureAfter(ZookeeperAutoConfiguration.class)
@AutoConfigureOrder(800)
public class ZookeeperLockAutoConfiguration {

    @Bean
    @ConditionalOnBean(CuratorFramework.class)
    public ZookeeperLockExecutor zookeeperLockExecutor(CuratorFramework curatorFramework, ApplicationContext applicationContext) {

        ZookeeperLockExecutor zookeeperLockExecutor = new ZookeeperLockExecutor(curatorFramework);
        if (applicationContext.containsBean("parentLockExecutor")) {
            zookeeperLockExecutor.setParent((LockExecutor<?>) applicationContext.getBean("parentLockExecutor"));
        }
        return zookeeperLockExecutor;
    }
}
