package com.admin4j.common.time;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author andanyang
 * @since 2023/4/26 17:06
 */
public class DateParseUtil {

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = OffsetTime.now().getOffset();

    /**
     * 解析 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parse(String dateStr) {
        return LocalDateTime.parse(dateStr, DateFormatterPattern.NORM_DATETIME_FORMATTER);
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成秒
     *
     * @param dateStr
     * @return
     */
    public static Long toEpochSecond(String dateStr, DateTimeFormatter dateTimeFormatter, String offset) {

        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZONE_OFFSET : ZoneOffset.of(offset);
        dateTimeFormatter = dateTimeFormatter == null ? DateFormatterPattern.NORM_DATETIME_FORMATTER : dateTimeFormatter;
        return LocalDateTime.parse(dateStr, dateTimeFormatter).toEpochSecond(zoneOffset);
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成秒
     *
     * @param dateStr
     * @return
     */
    public static Long toEpochSecond(String dateStr, String offset) {


        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZONE_OFFSET : ZoneOffset.of(offset);
        return LocalDateTime.parse(dateStr, DateFormatterPattern.NORM_DATETIME_FORMATTER).toEpochSecond(zoneOffset);
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成秒
     *
     * @param dateStr
     * @return
     */
    public static Long toEpochSecond(String dateStr) {

        return LocalDateTime.parse(dateStr, DateFormatterPattern.NORM_DATETIME_FORMATTER).toEpochSecond(DEFAULT_ZONE_OFFSET);
    }


    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成毫秒
     *
     * @param dateStr 时间
     * @return
     */
    public static Long toEpochMilli(String dateStr, DateTimeFormatter dateTimeFormatter, String offset) {

        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZONE_OFFSET : ZoneOffset.of(offset);
        dateTimeFormatter = dateTimeFormatter == null ? DateFormatterPattern.NORM_DATETIME_FORMATTER : dateTimeFormatter;
        return LocalDateTime.parse(dateStr, dateTimeFormatter).toInstant(zoneOffset).toEpochMilli();
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成秒
     *
     * @param dateStr
     * @return
     */
    public static Long toEpochMilli(String dateStr, String offset) {


        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZONE_OFFSET : ZoneOffset.of(offset);
        return LocalDateTime.parse(dateStr, DateFormatterPattern.NORM_DATETIME_FORMATTER).toInstant(zoneOffset).toEpochMilli();
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss 格式成秒
     *
     * @param dateStr
     * @return
     */
    public static Long toEpochMilli(String dateStr) {

        return LocalDateTime.parse(dateStr, DateFormatterPattern.NORM_DATETIME_FORMATTER).toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli();
    }

    /**
     * LocalDateTime 解释成秒
     *
     * @param localDateTime 本地日期
     * @param offset        时区
     * @return
     */
    public static Long toEpochSecond(LocalDateTime localDateTime, String offset) {

        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZONE_OFFSET : ZoneOffset.of(offset);
        return localDateTime.toEpochSecond(zoneOffset);
    }

    public static Long toEpochSecond(LocalDateTime localDateTime) {

        return localDateTime.toEpochSecond(DEFAULT_ZONE_OFFSET);
    }

    /**
     * LocalDateTime 解释毫秒
     *
     * @param localDateTime 本地日期
     * @param offset        时区
     * @return
     */
    public static Long toEpochMilli(LocalDateTime localDateTime, String offset) {

        ZoneOffset zoneOffset = StringUtils.isEmpty(offset) ? DEFAULT_ZONE_OFFSET : ZoneOffset.of(offset);
        return localDateTime.toInstant(zoneOffset).toEpochMilli();
    }

    public static Long toEpochMilli(LocalDateTime localDateTime) {

        return localDateTime.toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli();
    }
}
