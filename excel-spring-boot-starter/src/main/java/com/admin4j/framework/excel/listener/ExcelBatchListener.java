package com.admin4j.framework.excel.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * excel 分批处理
 * 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author andanyang
 */
@Slf4j
@Data
public class ExcelBatchListener<T> extends AnalysisEventListener<T> {

    protected List<T> list;

    /**
     * 每批大小
     */
    private int batchSize = 100;
    private Consumer<List<T>> consumer;

    public ExcelBatchListener(Consumer<List<T>> consumer) {
        this.consumer = consumer;
        this.list = new ArrayList<>(batchSize);
    }

    public ExcelBatchListener(int batchSize, Consumer<List<T>> consumer) {
        this.consumer = consumer;
        this.batchSize = batchSize;
        this.list = new ArrayList<>(batchSize);
    }

    //每一条数据解析都会来调用
    @Override
    public void invoke(T object, AnalysisContext context) {

        list.add(object);
        if (list.size() >= batchSize) {
            consumer.accept(list);
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!list.isEmpty()) {
            consumer.accept(list);
        }
    }
}