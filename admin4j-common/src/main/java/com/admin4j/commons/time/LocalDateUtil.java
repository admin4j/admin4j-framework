package com.admin4j.commons.time;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 * @author andanyang
 * @since 2023/4/26 17:06
 */
public class LocalDateUtil {

    static final ZoneOffset DEFAULT_ZOOM_OFFSET = OffsetTime.now().getOffset();

    /**
     * 解析 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    static public LocalDateTime parse(String dateStr) {
        return LocalDateTime.parse(dateStr, DateTimeFormatterConstant.NORM_DATETIME_FORMAT);
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成秒
     *
     * @param dateStr
     * @return
     */
    static public Long parse2EpochSecond(String dateStr, String offset) {


        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZOOM_OFFSET : ZoneOffset.of(offset);
        return LocalDateTime.parse(dateStr, DateTimeFormatterConstant.NORM_DATETIME_FORMAT).toEpochSecond(zoneOffset);
    }
}
