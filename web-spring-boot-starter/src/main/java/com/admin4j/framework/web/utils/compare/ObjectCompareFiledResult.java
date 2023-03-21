package com.admin4j.framework.web.utils.compare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对象字段比较结果
 *
 * @author andanyang
 * @since 2022/2/17 9:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectCompareFiledResult {

    /**
     * 比较字段
     */
    private String filed;
    /**
     * 字段描述
     */
    private String describe;
    /**
     * 原始值
     */
    private Object originValue;
    /**
     * 新值
     */
    private Object newValue;

    @Override
    public String toString() {
        return String.format("%s : %s -> %s", describe, originValue, newValue);
    }
}
