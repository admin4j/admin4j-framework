package com.admin4j.job.anno;

import com.admin4j.job.util.ScheduleConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 该注解只负责注册job，不负责删除任务
 *
 * @author andanyang
 * @since 2023/5/18 16:08
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JobRegister {

    String jobName();

    String jobDescription();

    String jobGroup() default "";

    String cronExpression();

    String misfirePolicy() default ScheduleConstants.MISFIRE_DEFAULT;

    boolean concurrent() default true;

    /**
     * 是否替换掉数据库里的数据
     * false: 有相同的job，以数据库为准
     * true： 覆盖数据库
     */
    boolean replace() default false;

    int status() default 0;
}
