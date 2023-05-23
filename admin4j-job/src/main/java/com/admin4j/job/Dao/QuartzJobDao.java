package com.admin4j.job.Dao;

import com.admin4j.job.enetity.form.QuartzJobQuery;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author andanyang
 * @since 2023/5/23 10:24
 */
public class QuartzJobDao {

    private JdbcTemplate jdbcTemplate;

    public void queryList(QuartzJobQuery QuartzJobQuery) {

        //String sql = "select * from sys_job where job_name = ? limit ?,?";
        //jdbcTemplate.query()
    }
}
