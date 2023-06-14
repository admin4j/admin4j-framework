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
    SysLogEvent generateEvent(String type, String content, String[] args);
 
}
