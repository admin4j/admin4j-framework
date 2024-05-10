package com.admin4j.framework.alert;

import com.admin4j.chatbot.qwx.QyWeiXinChatBot;
import com.admin4j.chatbot.qwx.core.msg.TextBotMsg;
import com.admin4j.common.exception.BizException;
import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.util.ServletUtils;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.alert.props.AlertProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 发送报警消息
 *
 * @author andanyang
 * @since 2024/5/8 14:13
 */
public class SendAlertMessageService {

    /**
     * 需要排除的异常
     */
    protected static final Set<Class<?>> EXCLUDE_EXS = new HashSet<Class<?>>() {
        private static final long serialVersionUID = -3265602846432901507L;

        {
            add(IllegalArgumentException.class);
            add(MaxUploadSizeExceededException.class);
            add(BindException.class);
            add(BizException.class);
        }
    };

    protected QyWeiXinChatBot qyWeiXinChatBot;
    @Value("${spring.application.name:''}")
    protected String applicationName;
    @Value("${spring.profiles.active:''}")
    protected String applicationEnv;
    @Autowired
    protected AlertProperties alertProperties;
    @Autowired
    protected Executor executor;

    /**
     * 发送报警消息
     *
     * @param name    报警名称
     * @param e       异常
     * @param message 显示消息详情
     */
    public void sendMsg(String name, String message, Throwable e) {


        if (e == null) {
            String alertMsg = name + "\n" +
                    "- appname: " + applicationName + "\n" +
                    "- env: " + applicationEnv + "\n" +
                    (message == null ? "" : "> message: " + message + "\n");
            doSendMsg(alertMsg);
            return;
        } else if (EXCLUDE_EXS.contains(e.getClass())) {
            return;
        }

        AuthenticationUser user = UserContextUtil.getUser();

        String url = "";

        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            url = request.getMethod() + " " + request.getRequestURI();
        }

        //错误堆栈
        StringBuilder cause = new StringBuilder();
        StringBuilder stackTraceSb = new StringBuilder();
        Throwable et = e;
        while (et != null) {

            cause.append(et.getClass().getSimpleName()).append(" message: ").append(et.getMessage()).append("\n");

            StackTraceElement[] stackTrace = et.getStackTrace();
            stackTraceSb.append("- ")
                    .append(Arrays.stream(stackTrace).limit(6).map(String::valueOf).collect(Collectors.joining("\n")));
            et = et.getCause();
        }
        
        String alertMsg = name + "\n" +

                "- appname: " + applicationName + "\n" +
                "- env: " + applicationEnv + "\n" +
                (message == null ? "" : "- message: " + message + "\n") +
                "- errorMsg: " + e.getMessage() + "\n" +
                "- Exception: " + e.getClass().getName() + "\n" +
                "- Cause: " + cause +
                "- url: " + url + "\n" +
                "- UserId: " + (user == null ? "" : user.getUserId()) + "\n" +
                "> " + stackTraceSb;

        doSendMsg(alertMsg);
    }

    protected void doSendMsg(String alertMsg) {
        executor.execute(() -> {
            TextBotMsg markdownBotMsg = new TextBotMsg(alertMsg);
            getChatBot().sendMsg(markdownBotMsg);
        });
    }

    protected QyWeiXinChatBot getChatBot() {
        if (qyWeiXinChatBot == null) {
            qyWeiXinChatBot = new QyWeiXinChatBot(alertProperties.getQyWeiXinWebhookUrl());
        }
        return qyWeiXinChatBot;
    }

}
