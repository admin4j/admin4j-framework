package com.admin4j.job.config;


import com.admin4j.job.anno.JobRegister;
import com.admin4j.job.enetity.QuartzJobInfo;
import com.admin4j.job.exception.JobException;
import com.admin4j.job.util.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author andanyang
 * @since 2023/5/18 16:12
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobBeanPostProcessor implements BeanPostProcessor {

    final Scheduler scheduler;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //注册job
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            JobRegister jobRegisterInfo = method.getAnnotation(JobRegister.class);
            if (jobRegisterInfo != null) {
                try {
                    registerJob(method, beanName, jobRegisterInfo);
                } catch (Exception e) {
                    log.error("JobRegister Annotation register error:" + e.getMessage(), e);
                    throw new JobException("JobRegister Annotation register error: " + e.getMessage(), e);
                }
            }
        }

        return bean;
    }

    private void registerJob(Method method, String beanName, JobRegister jobRegisterInfo) throws SchedulerException, ClassNotFoundException {

        QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
        quartzJobInfo.setJobName(jobRegisterInfo.jobName());
        quartzJobInfo.setJobGroup(StringUtils.isBlank(jobRegisterInfo.jobGroup()) ? null : jobRegisterInfo.jobGroup());
        quartzJobInfo.setJobDescription(jobRegisterInfo.jobDescription());
        quartzJobInfo.setConcurrent(jobRegisterInfo.concurrent());
        quartzJobInfo.setMisfirePolicy(jobRegisterInfo.misfirePolicy());
        quartzJobInfo.setConcurrent(jobRegisterInfo.concurrent());
        quartzJobInfo.setStatus(jobRegisterInfo.status());
        quartzJobInfo.setCronExpression(jobRegisterInfo.cronExpression());
        quartzJobInfo.setInvokeTarget(beanName + "." + method.getName() + "()");

        if (jobRegisterInfo.replace()) {
            ScheduleUtils.createJob(scheduler, quartzJobInfo, true);
        } else if (!ScheduleUtils.checkExists(scheduler, quartzJobInfo.getJobName(), quartzJobInfo.getJobGroup())) {
            ScheduleUtils.createJob(scheduler, quartzJobInfo);
        }
    }
}
