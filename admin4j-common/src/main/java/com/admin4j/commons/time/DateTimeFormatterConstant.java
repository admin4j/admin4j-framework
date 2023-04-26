package com.admin4j.commons.time;

import java.time.format.DateTimeFormatter;

/**
 * @author andanyang
 * @since 2023/4/26 17:01
 */
public class DateTimeFormatterConstant {

    private static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * UTC 2022-09-27T22:08:00.000Z
     */
    public final static DateTimeFormatter ISO_UTC_3MS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    /**
     * 两位小数
     * UTC 2022-09-27T22:08:00.00Z
     */
    public final static DateTimeFormatter ISO_UTC_2MS_FORMAT = DateTimeFormatter.ofPattern(UTC_PATTERN);
    public final static DateTimeFormatter ISO_UTC_FORMAT = DateTimeFormatter.ofPattern(UTC_PATTERN);

    public final static DateTimeFormatter NORM_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final static DateTimeFormatter NORM_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
