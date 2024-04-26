package com.admin4j.common.compare;

import com.admin4j.common.pojo.ResponseEnum;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author andanyang
 * @since 2022/2/17 8:53
 */
public class ObjectCompareUtil {

    private static final Comparator<Object> DEFAULT_FIELD_COMPARATOR;

    static {
        DEFAULT_FIELD_COMPARATOR = (o1, o2) -> {
            if (o1 instanceof BigDecimal) {
                return ((BigDecimal) o1).compareTo((BigDecimal) o2);
            }
            return Objects.equals(o1, o2) ? 0 : 1;
        };
    }

    /**
     * 比较对象
     *
     * @param originObj 原始对象
     * @param targetObj 目标对象
     * @return 字段比较结果列表
     * @throws IllegalAccessException 当访问字段权限不足时抛出此异常
     */
    public static List<ObjectCompareFiledResult> compareFields(Object originObj, Object targetObj) throws IllegalAccessException {
        return compareFields(originObj, targetObj, DEFAULT_FIELD_COMPARATOR);
    }

    /**
     * 比较对象
     *
     * @param originObj 原始对象
     * @param targetObj 目标对象
     * @return 字段比较结果列表
     * @throws IllegalAccessException 当访问字段权限不足时抛出此异常
     */
    public static List<ObjectCompareFiledResult> compareFields(Object originObj, Object targetObj, Comparator<Object> fieldComparator) throws IllegalAccessException {
        // 确保对象不为空
        ResponseEnum.VERIFY_ERROR.isTrue(ObjectUtils.allNotNull(originObj, targetObj), "obj is null");

        Class<?> originObjClass = originObj.getClass();
        // 确保对象类型相同
        ResponseEnum.VERIFY_ERROR.isTrue(originObjClass.equals(targetObj.getClass()), "Class is not equals");

        Field[] declaredFields = originObjClass.getDeclaredFields();

        ObjectCompare compareAnnotation = originObjClass.getAnnotation(ObjectCompare.class);
        boolean isCompareAllFields = (compareAnnotation != null && compareAnnotation.allFile());

        List<ObjectCompareFiledResult> results = new ArrayList<>(declaredFields.length / 2);
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];

            // 忽略不需要比较的字段
            ObjectCompareFiled fieldAnnotation = field.getAnnotation(ObjectCompareFiled.class);
            if (!isCompareAllFields && fieldAnnotation == null) {
                continue;
            }
            if (fieldAnnotation != null && fieldAnnotation.ignore()) {
                continue;
            }


            // 对字段设置为可访问，避免IllegalAccessException
            boolean accessible = field.isAccessible();
            // 注意：这可能影响安全性
            field.setAccessible(true);

            // 获取值
            Object originValue = field.get(originObj);
            Object newValue = field.get(targetObj);
            if (newValue == null && fieldAnnotation != null && fieldAnnotation.ignoreTargetNull()) {
                continue;
            }

            field.setAccessible(accessible);

            // 比较值
            if (fieldComparator.compare(originValue, newValue) == 0) {
                ObjectCompareFiledResult objectCompareFiledResult = new ObjectCompareFiledResult();

                objectCompareFiledResult.setField(field);
                objectCompareFiledResult.setFieldName(field.getName());
                objectCompareFiledResult.setDescribe(fieldAnnotation == null ? "" : fieldAnnotation.value());
                objectCompareFiledResult.setOriginValue(originValue);
                objectCompareFiledResult.setNewValue(newValue);
                results.add(objectCompareFiledResult);
            }
        }
        return results;
    }
}
