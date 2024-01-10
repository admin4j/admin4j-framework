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
     * @param originObj
     * @param targetObj
     * @return
     * @throws IllegalAccessException
     */
    public static List<ObjectCompareFiledResult> compareFields(Object originObj, Object targetObj) throws IllegalAccessException {

        ResponseEnum.VERIFY_ERROR.isTrue(ObjectUtils.allNotNull(originObj, targetObj), "obj is null");

        Class<?> originObjClass = originObj.getClass();

        ResponseEnum.VERIFY_ERROR.isTrue(originObjClass.equals(targetObj.getClass()), "Class is not equals");

        Field[] declaredFields = originObjClass.getDeclaredFields();

        ObjectCompare compareAnnotation = originObjClass.getAnnotation(ObjectCompare.class);
        boolean isCompareAllFields = (compareAnnotation != null && compareAnnotation.allFile());

        List<ObjectCompareFiledResult> results = new ArrayList<>(declaredFields.length / 2);
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];


            ObjectCompareFiled fieldAnnotation = field.getAnnotation(ObjectCompareFiled.class);
            if (!isCompareAllFields && fieldAnnotation == null) {
                continue;
            }
            if (fieldAnnotation != null && fieldAnnotation.ignore()) {
                continue;
            }


            String fieldName = field.getName();
            boolean accessible = field.isAccessible();
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

                objectCompareFiledResult.setFiled(fieldName);
                objectCompareFiledResult.setDescribe(fieldAnnotation == null ? "" : fieldAnnotation.value());
                objectCompareFiledResult.setOriginValue(originValue);
                objectCompareFiledResult.setNewValue(newValue);
                results.add(objectCompareFiledResult);
            }
        }
        return results;
    }
}
