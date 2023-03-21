package com.admin4j.framework.web.config;

/**
 * @author andanyang
 * tomcat 非法字符请求报错
 * @since 2021/5/28 13:57
 */
public class TomcatConfig {

    ///**
    // * 解决tomcat 非法字符请求报错
    // *
    // * @return ServletWebServerFactory
    // */
    //@Bean
    //@ConditionalOnClass(TomcatServletWebServerFactory.class)
    //public ConfigurableServletWebServerFactory webServerFactory() {
    //    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    //    factory.addConnectorCustomizers(connector -> {
    //                connector.setProperty("relaxedQueryChars", "|{}[]");
    //                connector.setProperty("relaxedPathChars", "|{}[]");
    //            }
    //    );
    //
    //    return factory;
    //}
}
