package com.admin4j.framework.excel.configuration;

import com.admin4j.framework.excel.ExcelUtils;
import com.admin4j.framework.excel.ExcelWriteLifecycle;
import com.alibaba.excel.read.listener.ReadListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/7/24 15:40
 */
public class ExcelAutoConfiguration implements InitializingBean {

    @Autowired(required = false)
    private ExcelWriteLifecycle excelWriteLifecycle;
    @Autowired(required = false)
    private List<ReadListener<Object>> readListeners;


    @Override
    public void afterPropertiesSet() throws Exception {


        ExcelUtils.init(excelWriteLifecycle, readListeners);
    }

}
