package com.admin4j.framework.alert.listener;

import com.admin4j.chatbot.qwx.QyWeiXinChatBot;
import com.admin4j.chatbot.qwx.core.msg.TextBotMsg;
import com.admin4j.common.exception.BizException;
import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.common.util.ServletUtils;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.alert.props.AlertProperties;
import com.admin4j.limiter.core.constant.LimiterType;
import com.admin4j.limiter.core.util.RateLimiterUtil;
import com.admin4j.spring.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author andanyang
 * @since 2024/2/22 11:17
 */
public class GlobalExceptionListener {

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
    @Autowired
    AlertProperties alertProperties;
    @Autowired
    Executor executor;

    public QyWeiXinChatBot getChatBot() {
        if (qyWeiXinChatBot == null) {
            qyWeiXinChatBot = new QyWeiXinChatBot(alertProperties.getQyWeiXinWebhookUrl());
        }
        return qyWeiXinChatBot;
    }

    public void sendMsg(GlobalExceptionEvent event) {

        Throwable e = event.getE();
        if (EXCLUDE_EXS.contains(e.getClass())) {
            return;
        }

        Environment environment = SpringUtils.getBean(Environment.class);
        String active = environment.getProperty("spring.profiles.active");
        String name = environment.getProperty("spring.application.name");
        StackTraceElement[] stackTrace = e.getStackTrace();
        String collect = Arrays.stream(stackTrace).limit(10).map(String::valueOf).collect(Collectors.joining("\n"));


        AuthenticationUser user = UserContextUtil.getUser();

        String url = "";

        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            url = request.getRequestURI();
        }
        String error = "GlobalExceptionHandler\n" +

                "- appname: " + name + "\n" +
                "- env: " + active + "\n" +
                "- message: " + e.getMessage() + "\n" +
                "- Exception: " + e.getClass().getName() + "\n" +
                "- url: " + url + "\n" +
                "- UserId: " + (user == null ? "" : user.getUserId()) + "\n" +
                "> " + collect;

        executor.execute(() -> {
            TextBotMsg markdownBotMsg = new TextBotMsg(error);
            getChatBot().sendMsg(markdownBotMsg);
        });


    }

    @TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.AFTER_COMMIT)
    public void globalException(GlobalExceptionEvent event) {


        // 限流
        String key = event.getName() + ":" + event.getE().getClass().getName() + ":" + event.getE().getMessage();

        key = "GlobalException:" + DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));

        if (RateLimiterUtil.rateLimiter(LimiterType.TOKEN_BUCKET, key, 20, 1)) {
            sendMsg(event);
        }
    }
}
