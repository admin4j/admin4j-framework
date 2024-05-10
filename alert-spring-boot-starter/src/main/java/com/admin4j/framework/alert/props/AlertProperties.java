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
     * 是否开启
     */
    private Boolean enable;
    /**
     * 应用启动失败告警
     */
    private boolean startupFailure = true;
    /**
     * 应用启动成功告警
     */
    private boolean startupSuccess = true;
    /**
     * 企业微信的 webhookUrl
     */
    private String qyWeiXinWebhookUrl;
}
