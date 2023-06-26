package com.admin4j.framework.log;

import com.admin4j.framework.log.event.SysLogEvent;

/**
 * @author andanyang
 * @since 2023/6/14 14:27
 */

public interface ISysLogService {

    /**
     * 保存日志
     *
     * @param sysLogEvent 日志参数
     */
    void saveLog(SysLogEvent sysLogEvent);

    /**
     * 同步参数生成事件
     *
     * @param type
     * @param content
     * @param args
     * @return
     */
    SysLogEvent generateEvent(String type, String content, String... args);

    default SysLogEvent generateEvent(ISysLogDO sysLogBO, String... args) {
        return generateEvent(sysLogBO.getType(), sysLogBO.getContent(), args);
    }

    /**
     * 保存日志
     *
     * @param type    日志类型
     * @param content 日志内容
     * @param args    日志参数
     */
    default void saveLog(String type, String content, String... args) {
        saveLog(generateEvent(type, content, args));
    }

    /**
     * 保存日志
     *
     * @param sysLogBO 日志内容
     * @param args     日志参数
     */
    default void saveLog(ISysLogDO sysLogBO, String... args) {
        saveLog(generateEvent(sysLogBO, args));
    }
}
