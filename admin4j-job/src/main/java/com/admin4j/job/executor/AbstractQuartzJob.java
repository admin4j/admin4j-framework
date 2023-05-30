package com.admin4j.job.executor;

import com.admin4j.job.enetity.QuartzJobInfo;
import com.admin4j.job.enetity.QuartzJobLog;
import com.admin4j.job.service.QuartzJobLogService;
import com.admin4j.job.util.ScheduleConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象quartz调用
 *
 * @author andanyoung
 */
public abstract class AbstractQuartzJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    @Autowired(required = false)
    private QuartzJobLogService quartzJobLogService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzJobInfo quartzJobInfo = (QuartzJobInfo) context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES);


        QuartzJobLog quartzJobLog = new QuartzJobLog();
        quartzJobLog.setJobName(quartzJobInfo.getJobName());
        quartzJobLog.setJobGroup(quartzJobInfo.getJobGroup());
        quartzJobLog.setStartTime(System.currentTimeMillis());
        quartzJobLog.setStatus(0);


        Exception exception = null;
        try {
            before(context, quartzJobLog);
            if (quartzJobInfo != null) {
                doExecute(context, quartzJobInfo);
            }

        } catch (Exception e) {
            exception = e;
            runException(context, quartzJobInfo, e);
        } finally {
            finalExecute(context, quartzJobLog, exception);
        }
    }

    private void finalExecute(JobExecutionContext context, QuartzJobLog quartzJobLog, Exception e) {

        if (quartzJobLogService != null) {

            quartzJobLog.setEndTime(System.currentTimeMillis());
            quartzJobLog.setDuration(quartzJobLog.getEndTime() - quartzJobLog.getStartTime());

            if (e == null) {
                quartzJobLog.setStatus(10);
                quartzJobLog.setExceptionInfo("");
            } else {
                quartzJobLog.setStatus(1);
                StringBuilder stringBuilder = new StringBuilder()
                        .append(" Exception: ").append(e.getClass().getName())
                        .append(" message: ").append(e.getMessage());
                if (e.getCause() != null) {
                    stringBuilder.append(" Cause:")
                            .append(e.getCause().getClass().getName());
                }
                quartzJobLog.setExceptionInfo(stringBuilder.toString());
            }

            quartzJobLogService.updateById(quartzJobLog);
        }

    }

    /**
     * 执行前,初始化数据
     *
     * @param context      工作执行上下文对象
     * @param quartzJobLog 系统计划任务
     */
    private void before(JobExecutionContext context, QuartzJobLog quartzJobLog) {

        if (quartzJobLogService != null) {
            //初始化数据
            quartzJobLogService.add(quartzJobLog);
        }
    }

    /**
     * 执行后
     *
     * @param context       工作执行上下文对象
     * @param quartzJobInfo 系统计划任务
     */
    protected void runException(JobExecutionContext context, QuartzJobInfo quartzJobInfo, Exception e) {
        log.error("任务执行异常 JobName:{}  JobGroup:{} errorMsg:{}", quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup(), e.getMessage(), e);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, QuartzJobInfo sysJob) throws Exception;
}
