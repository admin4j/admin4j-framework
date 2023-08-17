package com.admin4j.framework.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author andanyang
 * @since 2023/6/9 15:28
 */
public class BizServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 插入前执行
     *
     * @param entity 数据实体
     */
    protected void beforeSave(T entity) {

    }

    /**
     * 插入后执行
     * Params: entity – 数据实体
     */
    protected void afterSave(T entity) {

    }

    /**
     * 更新前执行
     *
     * @param entity 数据实体
     */
    protected void beforeUpdateById(T entity) {

    }

    /**
     * 更新后触发
     *
     * @param entity
     */
    protected void afterUpdateById(T entity) {
    }


    /**
     * 删除前执行
     *
     * @param id 主键id
     * @return ture 可以删除，false 不用删除
     */
    protected boolean beforeRemoveById(Serializable id) {

        return true;
    }

    /**
     * 删除后执行
     *
     * @param id 主键id
     */
    protected void afterRemoveById(Serializable id) {


    }

    @Override
    public boolean save(T entity) {
        beforeSave(entity);
        boolean save = super.save(entity);
        afterSave(entity);
        return save;
    }

    @Override
    public boolean updateById(T entity) {
        beforeUpdateById(entity);
        boolean b = super.updateById(entity);
        afterUpdateById(entity);
        return b;
    }


    @Override
    public boolean removeById(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!", new Object[0]);
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!", new Object[0]);
        Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());

        if (beforeRemoveById((Serializable) idVal)) {
            boolean b = super.removeById(entity);
            afterRemoveById((Serializable) idVal);
            return b;
        }
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        if (beforeRemoveById(id)) {
            boolean b = super.removeById(id);
            afterRemoveById(id);
            return b;
        }
        return true;
    }

    /**
     * 数据是否存在。如：name是否再数据库里存在
     *
     * @param filedFunction 比较字段
     * @param value         比较值
     * @return
     */
    public boolean exist(SFunction<T, Object> filedFunction, Object value) {

        LambdaQueryWrapper<T> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(filedFunction, value).last("LIMIT 1")
                .select(filedFunction);
        return getObj(queryWrapper, Object.class::cast) != null;
    }

    public boolean exist(T entity, SFunction<T, Object> filedFunction) {

        return exist(filedFunction, filedFunction.apply(entity));
    }

    /**
     * 数据是否存在。如：name是否再数据库里存在
     *
     * @param entity        实体
     * @param filedFunction 字段方法
     * @param keyFunction   主键方法
     * @return
     */
    public boolean exist(T entity, SFunction<T, Object> filedFunction, SFunction<T, Object> keyFunction) {

        LambdaQueryWrapper<T> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(filedFunction, filedFunction.apply(entity)).last("LIMIT 1")
                .select(keyFunction);
        Object obj = getObj(queryWrapper, Object.class::cast);
        if (obj == null) {
            return false;
        }

        return !Objects.equals(obj, keyFunction.apply(entity));
    }
}
