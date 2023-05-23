package com.admin4j.job.executor;

 
import com.admin4j.job.enetity.QuartzJobInfo;
import com.admin4j.job.util.JobInvokeUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author ruoyi
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, QuartzJobInfo sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
