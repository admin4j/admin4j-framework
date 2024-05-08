package com.admin4j.framework.alert.listener;

import com.admin4j.framework.alert.SendAlertMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * @author andanyang
 * @since 2024/5/8 14:07
 */
public class StartupListener {

    @Autowired
    private SendAlertMessageService sendAlertMessageService;

    @EventListener
    public void startupFailureListener(ApplicationFailedEvent event) {
        Throwable exception = event.getException();
        sendAlertMessageService.sendMsg("应用启动失败", null, exception);
    }

    @EventListener
    public void startupFailureListener(ApplicationReadyEvent event) {

        sendAlertMessageService.sendMsg("应用启动成功", "启动时间:" + event.getTimeTaken().toString(), null);
    }
}
