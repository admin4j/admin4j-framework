package com.admin4j.common;

import static java.lang.Integer.compare;

/**
 * 优先级接口可以由应该排序的对象实现，例如可执行队列中的任务。
 *
 * @author andanyang
 * @since 2023/4/26 9:39
 */
public interface Prioritized extends Comparable<Prioritized> {

    /**
     * The maximum priority
     */
    int MAX_PRIORITY = Integer.MIN_VALUE;

    /**
     * The minimum priority
     */
    int MIN_PRIORITY = Integer.MAX_VALUE;

    /**
     * Normal Priority
     */
    int NORMAL_PRIORITY = 0;

    /**
     * Get the priority
     *
     * @return the default is {@link #NORMAL_PRIORITY}
     */
    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    /**
     * 数字越大排序越靠前
     */
    @Override
    default int compareTo(Prioritized that) {
        return compare(this.getPriority(), that.getPriority());
    }

}
