package com.admin4j.framework.web.jackson;

import java.time.LocalDateTime;

/**
 * 时区格式化
 *
 * @author andanyang
 * @since 2024/3/27 19:10
 */
public interface TimeZoneFormatService {

    /**
     * 用户配置的时区key
     */
    String USER_TIME_ZONE_KEY = "TimeZone";

    LocalDateTime serialize(Long timestamp);
}
