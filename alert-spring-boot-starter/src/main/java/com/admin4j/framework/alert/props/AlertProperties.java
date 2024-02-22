package com.admin4j.framework.alert.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 告警配置
 *
 * @author andanyang
 * @since 2022/12/9 15:39
 */
@ConfigurationProperties(prefix = "admin4j.alert")
@Data
public class AlertProperties {


    /**
     * 企业微信的 webhookUrl
     */
    private String qyWeiXinWebhookUrl;
}
