package com.admin4j.job.enetity;


import com.admin4j.job.util.ScheduleConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author andanyang
 * @since 2023/5/18 9:40
 */
@Data
@Accessors(chain = true)
public class QuartzJobInfo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 任务名称
     */
    @ApiModelProperty(name = "任务名称、任务代号")
    private String jobName;
    @ApiModelProperty(name = "任务描述")
    private String jobDescription;

    /**
     * 任务组名
     */
    @ApiModelProperty(name = "任务组名")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @ApiModelProperty(name = "调用目标字符串")
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    @ApiModelProperty(name = "执行表达式 ")
    private String cronExpression;

    /**
     * cron计划策略
     */
    @ApiModelProperty(name = "计划策略 ", notes = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /**
     * 是否并发执行（1允许 0禁止）
     */
    @ApiModelProperty(name = "并发执行", notes = "1=允许,0=禁止")
    private boolean concurrent;

    /**
     * 任务状态（0正常 1暂停）
     */
    @ApiModelProperty(name = "任务状态", notes = "0=正常,1=暂停")
    private int status;
}
