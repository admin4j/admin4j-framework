package com.admin4j.framework.excel;

import java.util.List;

/**
 * excel 生命周期
 *
 * @author andanyang
 * @since 2023/7/24 15:28
 */
 
public interface ExcelReadLifecycle {

    /**
     * 读elex之前
     *
     * @param aClass – Excel aClass 头
     */
    <T> void before(Class<T> aClass);

    /**
     * 读完之后
     *
     * @param data   –数据列表哦
     * @param aClass – Excel aClass 头
     */
    <T> void after(List<T> data, Class<T> aClass);
}
