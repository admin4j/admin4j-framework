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
     * 企业微信机器人的 webhookUrl
     */
    private String qyWeiXinWebhookUrl;
    /**
     * 钉钉机器人的 webhookUrl
     */
    private String dingTalkWebhookUrl;
    /**
     * 钉钉机器人微信的 Secret
     */
    private String dingTalkSecret;

    /**
     * 限速 rateLimiterInterval 时间内可以发送的次数
     * 0 不限速
     */
    private int rateLimiterCapacity = 0;
    /**
     * 限速 时间间隔秒
     * 0 不限速
     */
    private int rateLimiterInterval = 0;

}
