package com.admin4j.job.mapper;


import com.admin4j.job.enetity.SysJob;
import com.admin4j.job.enetity.form.QuartzJobQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface SysJobMapper {
    /**
     * 查询调度任务日志集合
     *
     * @param job 调度信息
     * @return 操作日志集合
     */
    @Select("<script>select * from  sys_job  <if test=\"jobName != null and jobName != ''\" > where job_name = #{jobName} </if> limit #{pageNum, jdbcType=int}, #{pageSize, jdbcType=int};</script>")
    List<SysJob> selectJobList(QuartzJobQuery job);

    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    @Select("")
    List<SysJob> selectJobAll();

    /**
     * 通过调度ID查询调度任务信息
     *
     * @param jobId 调度ID
     * @return 角色对象信息
     */
    @Select("")
    SysJob selectJobById(Long jobId);

    /**
     * 通过调度ID删除调度任务信息
     *
     * @param jobId 调度ID
     * @return 结果
     */
    @Select("")
    int deleteJobById(Long jobId);

    /**
     * 批量删除调度任务信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Select("")
    int deleteJobByIds(Long[] ids);

    /**
     * 修改调度任务信息
     *
     * @param job 调度任务信息
     * @return 结果
     */
    @Select("")
    int updateJob(SysJob job);

    /**
     * 新增调度任务信息
     *
     * @param job 调度任务信息
     * @return 结果
     */
    @Select("")
    int insertJob(SysJob job);
}
