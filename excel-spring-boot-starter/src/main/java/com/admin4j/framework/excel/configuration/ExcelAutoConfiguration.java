package com.admin4j.framework.excel.configuration;

import com.admin4j.framework.excel.ExcelUtils;
import com.admin4j.framework.excel.ExcelWriteLifecycle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author andanyang
 * @since 2023/7/24 15:40
 */
public class ExcelAutoConfiguration implements InitializingBean {
    
    @Autowired(required = false)
    private ExcelWriteLifecycle excelWriteLifecycle;


    @Override
    public void afterPropertiesSet() throws Exception {


        if (excelWriteLifecycle != null) {
            ExcelUtils.setExcelWriteLifecycle(excelWriteLifecycle);
        }
    }

}
