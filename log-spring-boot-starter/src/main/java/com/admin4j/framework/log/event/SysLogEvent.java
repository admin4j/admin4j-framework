package com.admin4j.framework.log.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志数据
 *
 * @author andanyang
 * @since 2023/6/14 13:58
 */
@Data
public class SysLogEvent {
    private String type;
    private String content;
    private String[] args;
    private String page;
    private String uri = "";
    private String ip = "";
    private String userAgent = "";
    private long duration = 0;
    private String method = "";
    private String exception = "";
    private LocalDateTime createTime;
}
