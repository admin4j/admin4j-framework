package com.admin4j.framework.xxl.config;

import com.admin4j.framework.xxl.props.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

/**
 * @author andanyang
 * @Date 2021/7/7 11:29
 */
@Import(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

    @Autowired
    private XxlJobProperties xxlJobProperties;

    @Bean
    @ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enable", matchIfMissing = true)
    public XxlJobSpringExecutor xxlJobExecutor() {

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        if (StringUtils.hasText(xxlJobProperties.getAddress())) {
            xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
        }

        if (StringUtils.hasText(xxlJobProperties.getIp())) {
            xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        }

        if (xxlJobProperties.getPort() != 0) {
            xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        }

        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());

        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */

}
