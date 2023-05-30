package com.admin4j.job.controller;

import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.job.enetity.SysJob;
import com.admin4j.job.enetity.form.QuartzJobQuery;
import com.admin4j.job.service.ISysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/5/24 14:36
 */
@RestController
@RequestMapping("job")
public class SysJobController {

    @Autowired
    ISysJobService sysJobService;

    @GetMapping
    public SimpleResponse<List<SysJob>> info() {

        QuartzJobQuery quartzJobQuery = new QuartzJobQuery();
        List<SysJob> sysJobs = sysJobService.selectJobList(quartzJobQuery);
        SimpleResponse<List<SysJob>> quartzJobInfoSimpleResponse = new SimpleResponse<>();
        quartzJobInfoSimpleResponse.setData(sysJobs);
        return quartzJobInfoSimpleResponse;
    }
}
