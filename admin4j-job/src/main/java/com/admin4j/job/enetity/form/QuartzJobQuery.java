package com.admin4j.job.enetity.form;

import lombok.Data;

/**
 * @author andanyang
 * @since 2023/5/23 10:14
 */
@Data
public class QuartzJobQuery {

    private String jobName;
    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
