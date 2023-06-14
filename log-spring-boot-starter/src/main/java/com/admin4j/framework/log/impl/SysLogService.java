package com.admin4j.framework.log.impl;

import com.admin4j.common.util.ServletUtils;
import com.admin4j.framework.log.ISysLogService;
import com.admin4j.framework.log.annotation.SysLog;
import com.admin4j.framework.log.event.SysLogEvent;
import com.admin4j.spring.util.IpUtils;
import com.admin4j.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * @author andanyang
 * @since 2023/6/14 14:27
 */
@Slf4j
public class SysLogService implements ISysLogService {

    @Override
    public void saveLog(SysLogEvent sysLogEvent) {
        //只负责发送事件，不负责消费事件，具体时间由使用者实现
        SpringUtils.getApplicationContext().publishEvent(sysLogEvent);
    }

    @Override
    public SysLogEvent generateEvent(String type, String content, String[] args) {

        SysLogEvent sysLogEvent = new SysLogEvent();
        sysLogEvent.setType(type);
        sysLogEvent.setContent(content);
        sysLogEvent.setArgs(args);
        sysLogEvent.setCreateTime(LocalDateTime.now());

        //分析request对象
        javax.servlet.http.HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return sysLogEvent;
        }
        sysLogEvent.setUri(request.getRequestURI());
        String referer = request.getHeader("Referer");
        if (StringUtils.isNotBlank(referer)) {

            try {
                String path = new URL(referer).getPath();
                sysLogEvent.setPage(path);
            } catch (MalformedURLException e) {
                log.error("Referer error {}", referer);
            }
        }

        sysLogEvent.setIp(IpUtils.getIpAddr(request));
        sysLogEvent.setUserAgent(request.getHeader("User-Agent"));
        sysLogEvent.setMethod(request.getMethod());
        return sysLogEvent;
    }

    public SysLogEvent generateEvent(SysLog sysLog) {
        return generateEvent(sysLog.type(), sysLog.content(), sysLog.args());
    }
}
