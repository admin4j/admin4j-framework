package com.admin4j.framework.excel.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class ExcelListener<T> extends AnalysisEventListener<T> {

    protected List<T> data = new ArrayList<>();

    /**
     * 里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);
    }

    ///**
    // * 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
    // *
    // * @param exception
    // * @param context
    // * @throws Exception
    // */
    //@Override
    //public void onException(Exception exception, AnalysisContext context) throws Exception {
    //    super.onException(exception, context);
    //}

    //会读取每一行的数据
    @Override
    public void invoke(T object, AnalysisContext context) {
        data.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }
}