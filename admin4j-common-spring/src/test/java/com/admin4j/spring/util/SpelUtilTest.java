package com.admin4j.spring.util;

import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelParserConfiguration;

import java.lang.reflect.Method;

/**
 * @author andanyang
 * @since 2024/1/26 9:59
 */
public class SpelUtilTest extends TestCase {

    @Autowired
    SpelParserConfiguration spelParserConfiguration;

    public void testParse() throws NoSuchMethodException {

        // AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // SpelParserConfiguration bean = applicationContext.getBean(SpelParserConfiguration.class);


        System.out.println("spelParserConfiguration = " + spelParserConfiguration);
        Method[] methods = SpelUtilTest.class.getMethods();
        Method tMethod = SpelUtilTest.class.getDeclaredMethod("tMethod", String.class);
        SpelUtilTest spelUtilTest = new SpelUtilTest();


        String parse = SpelUtil.parse("'saleContract:'+#p1", tMethod, new Object[]{"param"});
        System.out.println("parse = " + parse);
    }

    public String tMethod(String id) {

        return "p0-12";
    }

    public void testTestParse() {
    }
}