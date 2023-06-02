package com.admin4j.framework.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 多渠道登录
 *
 * @author andanyang
 * @since 2023/6/1 16:50
 */
@Data
@ConfigurationProperties("admin4j.security.multi")
public class MultiAuthenticationProperties {

    /**
     * 是否开启多渠道登录
     */
    private boolean enable = true;
    /**
     * 多渠道登录uri 前缀
     */
    private String loginProcessingUrlPrefix = "/login/";
    /**
     * 多渠道登录是否只支持 post
     */
    private boolean postOnly = true;

    /**
     * 登录方式->用户名字段匹配
     * key 为登录方式 authType
     * value 为 前端接口传值 token的字段名
     * 比如手机号登录可以配置如下：
     * 请求url: /login/phone
     * key(authType)： phone
     * value: phoneNumber
     * <p>
     * 如果不配置 key为 phone 的数据，则会取 字段 phone（authType）
     */
    private Map<String, String> fieldMap;

}
