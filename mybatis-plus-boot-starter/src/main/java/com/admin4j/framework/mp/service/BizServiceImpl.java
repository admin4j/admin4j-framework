package com.admin4j.framework.mp.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;

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
     * 更新前执行
     *
     * @param entity 数据实体
     */
    protected void beforeUpdateById(T entity) {

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

    @Override
    public boolean save(T entity) {
        beforeSave(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        beforeUpdateById(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!", new Object[0]);
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!", new Object[0]);
        Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());

        if (beforeRemoveById((Serializable) idVal)) {
            return super.removeById(entity);
        }
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        if (beforeRemoveById(id)) {
            return super.removeById(id);
        }
        return true;
    }
}
