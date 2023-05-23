package com.admin4j.job.enetity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author andanyang
 * @since 2023/5/18 11:14
 */

@Data
public class QuartzJobLog {

    private Long id;
    private String jobName;
    private String jobGroup;
    private long startTime;
    private long endTime;
    @ApiModelProperty("任务运行时间")
    private long duration;
    @ApiModelProperty("job 运行结果 0：任务创建成功；1：任务运行失败；10 任务运行成功")
    private int status;
    private String exceptionInfo;
}
