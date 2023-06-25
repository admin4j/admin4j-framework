package com.admin4j.framework.xxl.util;

import com.xxl.job.core.context.XxlJobContext;

/**
 * @author andanyang
 * @since 2022/3/11 14:39
 */
public class XxlJobUtil {

    /**
     * 分片处理，当前分片是否可以处理
     *
     * @return
     */
    public static boolean canShare(long id) {

        if (Thread.currentThread().isInterrupted()) {
            return false;
        }
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        if (xxlJobContext == null) {
            return true;
        }

        int shardTotal = xxlJobContext.getShardTotal();
        int shardIndex = xxlJobContext.getShardIndex();
        return id % shardTotal == shardIndex;
    }

    /**
     * 分片处理，当前分片是否可以处理
     *
     * @return
     */
    public static boolean canShare(int id) {

        if (Thread.currentThread().isInterrupted()) {
            return false;
        }
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        if (xxlJobContext == null) {
            return true;
        }

        int shardTotal = xxlJobContext.getShardTotal();
        int shardIndex = xxlJobContext.getShardIndex();
        return id % shardTotal == shardIndex;
    }

    public static boolean canShare(String id) {

        int i = id.hashCode();
        int abs = Math.abs(i);
        return canShare(abs);
    }
}
