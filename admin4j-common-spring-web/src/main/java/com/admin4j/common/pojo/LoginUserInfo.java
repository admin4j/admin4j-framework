package com.admin4j.common.pojo;

/**
 * 基础用户数
 *
 * @author andanyang
 * @since 2023/6/7 9:05
 */
public interface LoginUserInfo {

    /**
     * @return 获取用户
     */
    Long getUserId();

    /**
     * @return 获取租户
     */
    Long getTenant();

    /**
     * 设置用户
     */
    void setUserId();

    /**
     * 设置租户
     */
    void setTenant();
}
