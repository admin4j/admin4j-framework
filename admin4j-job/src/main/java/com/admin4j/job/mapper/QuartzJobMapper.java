package com.admin4j.job.mapper;

import com.admin4j.job.enetity.form.QuartzJobQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author andanyang
 * @since 2023/5/24 14:45
 */
@Mapper
public interface QuartzJobMapper {

    @Select("select  count(0) from sys_job where ")
    int queryList(QuartzJobQuery quartzJobQuery);
}
