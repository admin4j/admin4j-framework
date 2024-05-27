package com.admin4j.framework.alert.send;

import com.admin4j.chatbot.dingtalk.DingRobot;
import com.admin4j.chatbot.dingtalk.request.MarkdownRobotRequest;
import com.admin4j.framework.alert.props.AlertProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 钉钉机器人报警
 *
 * @author andanyang
 * @since 2024/5/16 11:20
 */
@RequiredArgsConstructor
public class DingTalkAbstractSendAlertMessageService extends AbstractSendAlertMessageService {

    private final AlertProperties alertProperties;
    protected DingRobot chatBot;

    protected DingRobot getChatBot() {
        if (chatBot == null) {
            String dingTalkWebhookUrl = alertProperties.getDingTalkWebhookUrl();
            if (dingTalkWebhookUrl.contains("access_token")) {
                dingTalkWebhookUrl = StringUtils.substringAfterLast(dingTalkWebhookUrl, "access_token=");
            }
            chatBot = new DingRobot(dingTalkWebhookUrl, alertProperties.getDingTalkSecret());
        }
        return chatBot;
    }

    protected void doSendMsg(String title, String alertMsg) {
        executor.execute(() -> {
            MarkdownRobotRequest markdownBotMsg = new MarkdownRobotRequest(title, alertMsg);
            getChatBot().send(markdownBotMsg);
        });
    }
}
