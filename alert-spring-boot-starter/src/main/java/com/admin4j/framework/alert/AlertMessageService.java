package com.admin4j.framework.alert;

/**
 * @author andanyang
 * @since 2024/5/16 13:35
 */
public interface AlertMessageService {

    /**
     * 发送报警消息
     *
     * @param title   报警名称
     * @param e       异常
     * @param message 显示消息详情
     */
    void sendMsg(String title, String message, Throwable e);
}
