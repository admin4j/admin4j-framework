package com.admin4j.job.util;


import com.admin4j.job.enetity.QuartzJobInfo;
import com.admin4j.job.executor.QuartzDisallowConcurrentExecution;
import com.admin4j.job.executor.QuartzJobExecution;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author ruoyi
 */
public class ScheduleUtils {
    /**
     * 得到quartz任务类
     *
     * @param quartzJobInfo 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(QuartzJobInfo quartzJobInfo) throws ClassNotFoundException {

        String invokeTarget = quartzJobInfo.getInvokeTarget();
        if (StringUtils.endsWith(invokeTarget, ".class")) {
            return (Class<? extends Job>) QuartzJobInfo.class.getClassLoader().loadClass(StringUtils.substringBefore(invokeTarget, ".class"));
        } else {
            return quartzJobInfo.isConcurrent() ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
        }
    }


    public static void deleteJob(Scheduler scheduler, QuartzJobInfo quartzJobInfo) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup());
        JobKey jobKey = JobKey.jobKey(quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup());
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(jobKey);
    }

    public static boolean checkExists(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        return scheduler.checkExists(jobKey);
    }

    /**
     * 创建定时任务
     */
    public static void createJob(Scheduler scheduler, QuartzJobInfo quartzJobInfo, boolean replace) throws SchedulerException, ClassNotFoundException {

        String jobName = quartzJobInfo.getJobName();
        String jobGroup = quartzJobInfo.getJobGroup();

        JobDetail jobDetail = buildJobDetail(quartzJobInfo);

        CronTrigger cronTrigger = buildCronTrigger(quartzJobInfo);

        // 判断是否存在
        JobKey jobKey = JobKey.jobKey(quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            if (replace) {
                deleteJob(scheduler, quartzJobInfo);
            } else {
                throw new RuntimeException("Unable to create JobRegister : '" + jobName + "." + jobGroup + "' , because one already exists with this identification.");
            }
        }

        // 判断任务是否过期
        if (CronUtils.getNextExecution(quartzJobInfo.getCronExpression()) != null) {
            // 执行调度任务
            scheduler.scheduleJob(jobDetail, cronTrigger);
        }

        // 暂停任务
        if (quartzJobInfo.getStatus() == ScheduleConstants.Status.PAUSE.getValue()) {
            scheduler.pauseJob(jobKey);
        }
    }

    public static void createJob(Scheduler scheduler, QuartzJobInfo quartzJobInfo) throws SchedulerException, ClassNotFoundException {
        createJob(scheduler, quartzJobInfo, false);
    }

    /**
     * 更新 JOB
     */
    public static void updateJob(Scheduler scheduler, QuartzJobInfo quartzJobInfo) throws SchedulerException, ClassNotFoundException {
        createJob(scheduler, quartzJobInfo, true);
    }

    /**
     * 设置定时任务策略
     */
    private static CronScheduleBuilder handleCronScheduleMisfirePolicy(QuartzJobInfo job, CronScheduleBuilder cb) {
        switch (job.getMisfirePolicy()) {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new RuntimeException("The task misfire policy '" + job.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks");
        }
    }

    private static JobDetail buildJobDetail(QuartzJobInfo quartzJobInfo) throws ClassNotFoundException {

        Class<? extends Job> jobClass = getQuartzJobClass(quartzJobInfo);

        JobKey jobKey = JobKey.jobKey(quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup());
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey)
                .withDescription(quartzJobInfo.getJobDescription()).build();
        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, quartzJobInfo);
        return jobDetail;
    }

    private static CronTrigger buildCronTrigger(QuartzJobInfo quartzJobInfo) {

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJobInfo.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(quartzJobInfo, cronScheduleBuilder);

        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup());
        // 按新的cronExpression表达式构建一个新的trigger
        return TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder).build();
    }
}
