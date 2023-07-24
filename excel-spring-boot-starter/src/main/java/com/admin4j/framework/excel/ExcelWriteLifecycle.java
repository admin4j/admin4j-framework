package com.admin4j.framework.excel;

import java.util.List;

/**
 * excel 生命周期
 *
 * @author andanyang
 * @since 2023/7/24 15:28
 */
public interface ExcelWriteLifecycle {

    /**
     * 写elex之前
     * data –数据列表哦
     * aClass – Excel aClass 头
     */
    <T> void before(List<T> data, Class<T> aClass);

    /**
     * 写完之后
     * data –数据列表哦
     * aClass – Excel aClass 头
     */
    <T> void after(List<T> data, Class<T> aClass);
}
