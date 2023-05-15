package com.admin4j.common.pojo;

/**
 * @author andanyang
 * @since 2021/7/29 16:18
 */
public interface IResponse {
    /**
     * 返回码
     *
     * @return int
     */
    int getCode();

    /**
     * 返回消息
     *
     * @return String
     */
    String getMsg();
}
