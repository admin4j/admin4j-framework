package com.admin4j.framework.feign;

import com.admin4j.framework.feign.properties.FeignClientBeanProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * @author andanyang
 * @since 2023/12/22 9:19
 */
public class FeignClientFactory {

    public static FeignClientFactoryBean feignClientFactoryBean(FeignClientBeanProperties feignClientBeanProperties, ApplicationContext applicationContext, BeanFactory beanFactory) {

        FeignClientFactoryBean factoryBean = new FeignClientFactoryBean();
        factoryBean.setBeanFactory(beanFactory);
        factoryBean.setApplicationContext(applicationContext);

        factoryBean.setContextId(feignClientBeanProperties.getAppName());
        factoryBean.setUrl(feignClientBeanProperties.getUrl());
        factoryBean.setPath(feignClientBeanProperties.getPath());
        factoryBean.setFallbackFactory(feignClientBeanProperties.getFallbackFactory());
        factoryBean.setType(feignClientBeanProperties.getType());
        if (StringUtils.hasText(feignClientBeanProperties.getBeanName())) {
            factoryBean.setName(feignClientBeanProperties.getBeanName());
        } else {
            factoryBean.setName("feignClient#" + feignClientBeanProperties.getType().getCanonicalName());
        }
        return factoryBean;
    }
}
