package com.admin4j.job.service;


import com.admin4j.job.enetity.QuartzJobLog;

/**
 * @author andanyang
 * @since 2023/5/18 11:14
 */
public interface QuartzJobLogService {
    void add(QuartzJobLog quartzJobLog);

    void updateById(QuartzJobLog quartzJobLog);
}
