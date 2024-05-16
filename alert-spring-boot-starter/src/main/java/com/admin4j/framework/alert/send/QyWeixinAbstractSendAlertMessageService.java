package com.admin4j.framework.alert.send;

import com.admin4j.chatbot.qwx.QyWeiXinChatBot;
import com.admin4j.chatbot.qwx.core.msg.TextBotMsg;
import com.admin4j.framework.alert.props.AlertProperties;
import lombok.RequiredArgsConstructor;

/**
 * 企微机器人报警
 *
 * @author andanyang
 * @since 2024/5/16 11:20
 */
@RequiredArgsConstructor
public class QyWeixinAbstractSendAlertMessageService extends AbstractSendAlertMessageService {

    protected final AlertProperties alertProperties;
    protected QyWeiXinChatBot qyWeiXinChatBot;

    protected QyWeiXinChatBot getChatBot() {
        if (qyWeiXinChatBot == null) {
            qyWeiXinChatBot = new QyWeiXinChatBot(alertProperties.getQyWeiXinWebhookUrl());
        }
        return qyWeiXinChatBot;
    }

    protected void doSendMsg(String title, String alertMsg) {
        executor.execute(() -> {
            TextBotMsg markdownBotMsg = new TextBotMsg(alertMsg);
            getChatBot().sendMsg(markdownBotMsg);
        });
    }
}
