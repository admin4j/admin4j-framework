package com.admin4j.framework.mp.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * 写接口
 * CRQS 模式。读写分离
 *
 * @author andanyang
 * @since 2023/8/17 11:00
 */
public interface ICommandService<T> {

    /**
     * 获取对应 entity 的 BaseMapper
     *
     * @return BaseMapper
     */
    BaseMapper<T> getBaseMapper();


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    boolean save(T entity);


    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    boolean saveOrUpdate(T entity);


    //---------------------------------------------------------------- update ------------------------

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    boolean updateById(T entity);


    //---------------------------------------------------------------- remove ------------------------

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    boolean removeById(Serializable id);

    /**
     * 根据 ID 删除
     *
     * @param id      主键(类型必须与实体类型字段保持一致)
     * @param useFill 是否启用填充(为true的情况,会将入参转换实体进行delete删除) 标记删除
     * @return 删除结果
     * @since 3.5.0
     */
    boolean removeById(Serializable id, boolean useFill);
}
