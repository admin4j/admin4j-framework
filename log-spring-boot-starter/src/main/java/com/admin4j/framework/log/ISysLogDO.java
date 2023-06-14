package com.admin4j.framework.log;

/**
 * @author andanyang
 * @since 2023/6/14 15:45
 */
public interface ISysLogDO {

    /**
     * 日志类型
     *
     * @return {String}
     */
    String getType();

    /**
     * 日志详情 spel 表达式
     *
     * @return 日志描述
     */
    String getContent();

//    /**
//     * @return 日志参数
//     */
//    String[] getArgs();
}
