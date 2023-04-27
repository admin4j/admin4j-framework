package com.admin4j.framework.xss;


import com.admin4j.framework.xss.filter.XssFilter;
import com.admin4j.framework.xss.listener.EnvironmentChangeListener;
import com.admin4j.framework.xss.property.XssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2021/10/22 16:47
 */
@EnableConfigurationProperties(XssProperties.class)
@ConditionalOnProperty(value = "admin4j.xss.match-pattern")
public class XssAuthConfiguration {
    /**
     * 配置跨站攻击 反序列化处理器
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    //@Bean
    //public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer2() {
    //    return builder -> builder.deserializerByType(String.class, new XssStringJsonDeserializer());
    //}
    @Bean
    public XssFilter xssFilter(XssProperties xssProperties) {
        return new XssFilter(xssProperties);
    }

    @Bean
    @ConditionalOnClass(name = {"org.springframework.cloud.context.environment.EnvironmentChangeEvent"})
    public EnvironmentChangeListener environmentChangeListener() {
        return new EnvironmentChangeListener();
    }

    /**
     * 配置跨站攻击过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean(XssFilter xssFilter) {


        FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>(xssFilter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }
}
