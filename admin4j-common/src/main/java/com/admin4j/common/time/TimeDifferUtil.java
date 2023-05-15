package com.admin4j.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author andanyang
 * @since 2022/3/26 16:48
 */
public class TimeDifferUtil {

    /**
     * 相差几天
     *
     * @param beginDate beginTime
     * @param endDate   endDate
     * @return 相差几天
     */
    public static long day(LocalDate beginDate, LocalDate endDate) {

        return endDate.toEpochDay() - beginDate.toEpochDay();
    }

    /**
     * 相差几年
     *
     * @param beginDate beginTime
     * @param endDate   endDate
     * @return 相差几天
     */
    public static double year(LocalDate beginDate, LocalDate endDate) {

        return (endDate.toEpochDay() - beginDate.toEpochDay()) / 365.0;
    }

    /**
     * 相差几天
     *
     * @param beginTime beginTime
     * @param endTime   endTime
     * @return 相差几天
     */
    public static double day(LocalDateTime beginTime, LocalDateTime endTime) {


        long beginSecond = beginTime.toEpochSecond(ZoneOffset.UTC);
        long endSecond = endTime.toEpochSecond(ZoneOffset.UTC);
        return (endSecond - beginSecond) / 60.0 / 60.0 / 24.0;
    }


    /**
     * 相差几小时
     */
    public static double hour(LocalDateTime beginTime, LocalDateTime endTime) {


        long beginSecond = beginTime.toEpochSecond(ZoneOffset.UTC);
        long endSecond = endTime.toEpochSecond(ZoneOffset.UTC);
        return (endSecond - beginSecond) / 60.0 / 60.0;
    }

    /**
     * 相差几分钟
     */
    public static double minute(LocalDateTime beginTime, LocalDateTime endTime) {

        long beginSecond = beginTime.toEpochSecond(ZoneOffset.UTC);
        long endSecond = endTime.toEpochSecond(ZoneOffset.UTC);
        return (endSecond - beginSecond) / 60.0;
    }

    /**
     * 相差几秒
     */
    public static double second(LocalDateTime beginTime, LocalDateTime endTime) {

        long beginSecond = beginTime.toEpochSecond(ZoneOffset.UTC);
        long endSecond = endTime.toEpochSecond(ZoneOffset.UTC);
        return (endSecond - beginSecond);
    }


}
