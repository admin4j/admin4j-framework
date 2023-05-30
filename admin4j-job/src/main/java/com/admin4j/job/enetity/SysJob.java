package com.admin4j.job.enetity;

import com.admin4j.job.util.CronUtils;
import com.admin4j.job.util.ScheduleConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 定时任务调度表 sys_job
 *
 * @author ruoyi
 */
@Data
public class SysJob {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */

    private Long jobId;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 64, message = "任务名称不能超过64个字符")
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @NotBlank(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 500, message = "调用目标字符串长度不能超过500个字符")
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    private String cronExpression;

    /**
     * cron计划 过期后的执行策略 misfire，0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行
     */
    private Integer misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;


    private Integer concurrent;

    /**
     * 任务状态（0正常 1暂停）
     */
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }
}
