package com.admin4j.spring.util;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author andanyang
 * @since 2023/5/23 14:20
 */


public class SpringRegisterUtilTest extends TestCase {

    @Test
    public void testRegisterBean() {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        applicationContext.registerShutdownHook();
        SpringUtils bean = applicationContext.getBean(SpringUtils.class);
        System.out.println("bean = " + bean);
        SpringRegisterUtil.registerBean(IpUtils.class, "test");
        IpUtils bean1 = applicationContext.getBean(IpUtils.class);
    }
}