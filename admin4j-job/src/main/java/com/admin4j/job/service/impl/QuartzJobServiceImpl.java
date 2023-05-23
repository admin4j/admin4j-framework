package com.admin4j.job.service.impl;

import com.admin4j.job.enetity.QuartzJobInfo;
import com.admin4j.job.service.QuartzJobService;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/5/23 10:13
 */
public class QuartzJobServiceImpl implements QuartzJobService {

    /**
     * 获取quartz调度器的计划任务
     *
     * @param job 调度信息
     * @return 调度任务集合
     */
    @Override
    public List<QuartzJobInfo> selectJobList(QuartzJobInfo job) {
        return null;
    }

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public QuartzJobInfo selectJobById(Long jobId) {
        return null;
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public int pauseJob(QuartzJobInfo job) throws SchedulerException {
        return 0;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public int resumeJob(QuartzJobInfo job) throws SchedulerException {
        return 0;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public int deleteJob(QuartzJobInfo job) throws SchedulerException {
        return 0;
    }

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Override
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {

    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public int changeStatus(QuartzJobInfo job) throws SchedulerException {
        return 0;
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public boolean run(QuartzJobInfo job) throws SchedulerException {
        return false;
    }

    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public int insertJob(QuartzJobInfo job) throws SchedulerException {
        return 0;
    }

    /**
     * 更新任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public int updateJob(QuartzJobInfo job) throws SchedulerException {
        return 0;
    }

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return false;
    }
}
