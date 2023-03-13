package com.admin4j.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于储存成对出现的简单数据
 * 比如再数据库里只查询，两个字段
 *
 * @author andanyang
 * @since 2021/7/27 11:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PairVo<K, V> {

    private K key;
    private V value;

    public static <K, V> PairVo<K, V> of(K key, V value) {
        PairVo<K, V> pairVo = new PairVo<>();
        pairVo.setKey(key);
        pairVo.setValue(value);
        return pairVo;
    }
}
