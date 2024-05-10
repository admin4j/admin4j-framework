package com.admin4j.framework.alert.listener;

import com.admin4j.framework.alert.SendAlertMessageService;
import com.admin4j.framework.alert.props.AlertProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * 监听启动事件
 *
 * @author andanyang
 * @since 2024/5/8 14:07
 */
@Slf4j
public class StartupListener {

    @Autowired
    private SendAlertMessageService sendAlertMessageService;
    @Autowired
    private AlertProperties alertProperties;

    /**
     * 监听启动成功事件
     *
     * @param event
     */
    @EventListener
    public void applicationReadyEvent(ApplicationReadyEvent event) {

        if (!alertProperties.isStartupSuccess()) {
            return;
        }
        try {
            sendAlertMessageService.sendMsg("应用启动成功", "启动时间:" + event.getTimeTaken().toString(), null);
        } catch (Exception e) {
            log.error("startupFailureListener 发送消息失败", e);
        }
    }


    /**
     * 监听启动失败事件
     *
     * @param event
     */
    @EventListener
    public void startupFailureListener(ApplicationFailedEvent event) {


        if (!alertProperties.isStartupFailure()) {
            return;
        }
        Throwable exception = event.getException();
        try {
            sendAlertMessageService.sendMsg("应用启动失败", null, exception);
        } catch (Exception e) {
            log.error("startupFailureListener 发送消息失败", e);
        }

    }
}
