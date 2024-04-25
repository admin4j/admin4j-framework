package com.admin4j.common.compare;

import com.admin4j.common.pojo.ResponseEnum;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author andanyang
 * @since 2022/2/17 8:53
 */
public class ObjectCompareUtil {

    /**
     * 比较对象
     *
     * @param originObj 原始对象
     * @param targetObj 目标对象
     * @return 字段比较结果列表
     * @throws IllegalAccessException 当访问字段权限不足时抛出此异常
     */
    public static List<ObjectCompareFiledResult> compareFields(Object originObj, Object targetObj) throws IllegalAccessException {
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
            if (!Objects.equals(originValue, newValue)) {
                ObjectCompareFiledResult objectCompareFiledResult = new ObjectCompareFiledResult();

                objectCompareFiledResult.setField(field);
                //objectCompareFiledResult.setFieldName(fieldName);
                objectCompareFiledResult.setDescribe(fieldAnnotation == null ? "" : fieldAnnotation.value());
                objectCompareFiledResult.setOriginValue(originValue);
                objectCompareFiledResult.setNewValue(newValue);
                results.add(objectCompareFiledResult);
            }
        }
        return results;
    }
}
