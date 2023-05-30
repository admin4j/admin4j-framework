package com.admin4j.job.executor;


import com.admin4j.job.enetity.QuartzJobInfo;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author andanyang
 * @since 2023/5/18 11:06
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    /**
     * 执行方法，由子类重载
     *
     * @param context       工作执行上下文对象
     * @param quartzJobInfo 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    @Override
    protected void doExecute(JobExecutionContext context, QuartzJobInfo quartzJobInfo) throws Exception {
//        JobInvokeUtil.invokeMethod(quartzJobInfo);
    }
}
