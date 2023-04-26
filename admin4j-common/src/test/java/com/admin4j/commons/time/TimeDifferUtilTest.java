package com.admin4j.commons.time;

import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author andanyang
 * @since 2023/4/26 17:40
 */
public class TimeDifferUtilTest {

    @Test
    public void testDay() {

        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = begin.plusHours(2).minusMinutes(1);
        double minute = TimeDifferUtil.minute(begin, end);
        System.out.println("minute = " + minute);

        double second = TimeDifferUtil.second(begin, end);
        System.out.println("second = " + second);

        System.out.println("TimeDifferUtil.day(begin,end) = " + TimeDifferUtil.day(begin, end));
    }
}