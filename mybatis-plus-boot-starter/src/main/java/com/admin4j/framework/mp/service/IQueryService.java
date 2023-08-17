package com.admin4j.framework.mp.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 读接口
 * CRQS 模式。读写分离
 *
 * @author andanyang
 * @since 2023/8/17 10:59
 */
public interface IQueryService<T> {

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    T getById(Serializable id);


    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    List<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    List<T> list(Wrapper<T> queryWrapper);

    /**
     * 查询列表
     *
     * @param entity 实体
     */
    List<T> list(T entity);

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    List<T> list();

    /**
     * 无条件翻页查询
     *
     * @param page 翻页对象
     * @see Wrappers#emptyWrapper()
     */
    <E extends IPage<T>> E page(E page);

    /**
     * 翻页查询
     *
     * @param page   翻页对象
     * @param entity 实体
     */
    <E extends IPage<T>> E page(E page, T entity);

    <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper);
}
