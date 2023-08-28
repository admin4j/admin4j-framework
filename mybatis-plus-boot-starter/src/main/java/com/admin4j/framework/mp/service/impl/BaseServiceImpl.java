package com.admin4j.framework.mp.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author andanyang
 * @since 2023/8/17 11:04
 */

abstract class BaseServiceImpl<M extends BaseMapper<T>, T> {
    protected Log log = LogFactory.getLog(getClass());

    protected M baseMapper;
    protected Class<M> mapperClass = currentMapperClass();

    protected Class<T> entityClass = currentModelClass();


    public M getBaseMapper() {
        return baseMapper;
    }

    @Autowired
    protected void setBaseMapper(M baseMapper) {
        this.baseMapper = baseMapper;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    protected Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseServiceImpl.class, 0);
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseServiceImpl.class, 1);
    }
}
