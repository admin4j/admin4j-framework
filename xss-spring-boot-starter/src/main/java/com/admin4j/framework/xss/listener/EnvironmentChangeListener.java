package com.admin4j.framework.xss.listener;

import com.admin4j.framework.xss.filter.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;

/**
 * @author andanyang
 * @since 2023/4/27 14:15
 */
public class EnvironmentChangeListener {

    @Autowired
    XssFilter xssFilter;

    @EventListener(EnvironmentChangeEvent.class)
    public void environmentChangeEvent(EnvironmentChangeEvent event) {
        /**
         * 刷新配置
         */
        xssFilter.refresh();
    }
}
