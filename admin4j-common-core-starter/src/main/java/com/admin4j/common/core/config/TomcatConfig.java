package com.admin4j.common.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * tomcat 非法字符请求报错
 * @since 2021/5/28 13:57
 */
public class TomcatConfig {

    /**
     * 解决tomcat 非法字符请求报错
     *
     * @return ServletWebServerFactory
     */
    @Bean
    @ConditionalOnClass(TomcatServletWebServerFactory.class)
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
                    connector.setProperty("relaxedQueryChars", "|{}[]");
                    connector.setProperty("relaxedPathChars", "|{}[]");
                }
        );

        return factory;
    }
}
