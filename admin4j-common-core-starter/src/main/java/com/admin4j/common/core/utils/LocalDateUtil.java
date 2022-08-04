package com.admin4j.common.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author andanyang
 * @since 2021/12/14 10:09
 */
public final class LocalDateUtil {


    /**
     * 解析 yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    static public LocalDateTime parse(String data) {
        return LocalDateTime.parse(data, DateTimePattern.NORM_DATETIME_FORMATTER);
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    static public Long parse2EpochSecond(String data, String offset) {
        offset = StringUtils.isEmpty(offset) ? "+8" : offset;
        return LocalDateTime.parse(data, DateTimePattern.NORM_DATETIME_FORMATTER).toEpochSecond(ZoneOffset.of(offset));
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    static public Long parse2Millisecond(String data, String offset) {
        offset = StringUtils.isEmpty(offset) ? "+8" : offset;
        return LocalDateTime.parse(data, DateTimePattern.NORM_DATETIME_FORMATTER).toEpochSecond(ZoneOffset.of(offset)) * 1000;
    }

    /**
     * 解析 yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    static public Long parse2Millisecond(String data) {
        return parse2Millisecond(data, null);
    }

    static public Long parse2Millisecond(LocalDateTime localDateTime, String offset) {
        offset = StringUtils.isEmpty(offset) ? "+8" : offset;
        return localDateTime.toEpochSecond(ZoneOffset.of(offset)) * 1000;
    }

    static public Long parse2Millisecond(LocalDateTime localDateTime) {

        return parse2Millisecond(localDateTime, null);
    }


    /**
     * 相差几天
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    static public double differDay(LocalDateTime beginTime, LocalDateTime endTime) {


        long beginSecond = beginTime.toEpochSecond(ZoneOffset.UTC);
        long endSecond = endTime.toEpochSecond(ZoneOffset.UTC);
        return (endSecond - beginSecond) / 60.0 / 60.0 / 24.0;
    }
}
