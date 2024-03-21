package com.admin4j.framework.feign.properties;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * @author andanyang
 * @since 2023/12/22 9:23
 */
@Data
public class FeignClientBeanProperties {
    // 请求服务名称
    private String appName;
    // 请求url
    private String url;
    // 请求路径
    private String path;
    private String beanName;
    private @NotNull Class<?> type;
    private Class<?> fallbackFactory;
}
