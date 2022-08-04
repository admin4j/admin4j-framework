package com.admin4j.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiPredicate;

/**
 * 分页查询工具
 *
 * @author andanyang
 * @since 2022/6/15 11:46
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtil {

    /**
     * 分页循环-按每页数量分
     *
     * @param limit       每页限制几条
     * @param biPredicate
     */
    public static void foreach(int limit, BiPredicate<Integer, Integer> biPredicate) {
        int offset = 0;
        int pageNum = 0;
        boolean hasMore = true;
        do {
            offset = pageNum * limit;
            hasMore = biPredicate.test(offset, limit);
            pageNum++;
        } while (hasMore);
    }

    /**
     * 分页循环-按页数分
     *
     * @param pageNum     从第几页开始
     * @param pageSize
     * @param biPredicate
     */
    public static void foreachPageNum(int pageNum, int pageSize, BiPredicate<Integer, Integer> biPredicate) {

        boolean hasMore = true;
        do {

            hasMore = biPredicate.test(pageNum, pageSize);
            pageNum++;
        } while (hasMore);
    }

    /**
     * 分页循环-按页数分
     * 默认 从第1页开始
     *
     * @param pageSize
     * @param biPredicate
     */
    public static void foreachPageNum(int pageSize, BiPredicate<Integer, Integer> biPredicate) {

        foreachPageNum(1, pageSize, biPredicate);
    }
}
