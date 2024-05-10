package com.admin4j.framework.alert.listener;

import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.framework.alert.SendAlertMessageService;
import com.admin4j.limiter.core.constant.LimiterType;
import com.admin4j.limiter.core.util.RateLimiterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author andanyang
 * @since 2024/2/22 11:17
 */
public class GlobalExceptionListener {

    @Autowired
    private SendAlertMessageService sendAlertMessageService;

    /**
     * 监听全局异常
     *
     * @param event
     */
    @TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.AFTER_COMMIT)
    public void globalException(GlobalExceptionEvent event) {


        // 限流
        String key = event.getName() + ":" + event.getE().getClass().getName() + ":" + event.getE().getMessage();

        key = "GlobalException:" + DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));

        if (RateLimiterUtil.rateLimiter(LimiterType.TOKEN_BUCKET, key, 20, 1)) {
            sendAlertMessageService.sendMsg(event.getName(), event.getMessage(), event.getE());
        }
    }

    @TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.AFTER_COMMIT)
    public void startupFailureListener(ApplicationFailedEvent event) {
        Throwable exception = event.getException();
        sendAlertMessageService.sendMsg("应用启动失败", null, exception);
    }
}
