package com.admin4j.commons;

import junit.framework.TestCase;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author andanyang
 * @since 2023/4/26 15:56
 */
public class TargetTypeTest extends TestCase {

    @Test
    public void testGetClassType() {
        TargetType<TargetTypeTest> appTestTargetType = new TargetType<>();
        Class<TargetTypeTest> classType = appTestTargetType.getClassType();
        System.out.println("classType = " + classType);
    }

    @Test
    public void testGetClassTypeV() {

        TargetType<List<TargetTypeTest>> targetType = new TargetType<List<TargetTypeTest>>() {
        };

        //        new TargetType<List<TargetTypeTest>>() {
        //};
        Type type = targetType.getType();
        System.out.println("type = " + type);
        Class<List<TargetTypeTest>> classType1 = targetType.getClassType();
        System.out.println("classType1 = " + classType1);

        //List<TargetTypeTest> list = new ArrayList<TargetTypeTest>();

        //String a = testGetClassTypeT();

        TargetType<List<TargetTypeTest>> appTestTargetType = new TargetType<>();
        Class<List<TargetTypeTest>> classType = appTestTargetType.getClassType();
        System.out.println("classType = " + classType);
    }


    //public <T> T testGetClassTypeT() {
    //    //TargetType<T> targetType = new TargetType<T>() {
    //    //};
    //    //Type type = targetType.getType();
    //    //System.out.println("type = " + type);
    //    //Class<T> classType1 = targetType.getClassType();
    //    //System.out.println("classType1 = " + classType1);
    //    //
    //    //try {
    //    //    return classType1.newInstance();
    //    //} catch (IllegalAccessException e) {
    //    //    throw new RuntimeException(e);
    //    //} catch (InstantiationException e) {
    //    //    throw new RuntimeException(e);
    //    //}
    //}
}