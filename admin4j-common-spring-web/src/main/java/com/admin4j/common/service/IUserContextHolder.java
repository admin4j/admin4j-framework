package com.admin4j.common.service;

import com.admin4j.common.pojo.AuthenticationUser;

/**
 * @author andanyang
 * @since 2023/6/6 18:06
 */
public interface IUserContextHolder extends ILoginUserInfoService, ILoginTenantInfoService {

    /**
     * 是否登录
     *
     * @return boolean 是否登录
     */
    boolean isLogin();

    /**
     * 当前会话注销登录
     */
    void loginOut();

    /**
     * 设置用户ID
     */
    void setUserId(Long userId);

    /**
     * 设置租户
     */

    void setTenant(Long tenant);

    /**
     * 关闭租户
     */
    void offTenant();

    /**
     * 设置登录者信息
     *
     * @param authenticationUser 登录用户
     */
    void setAuthenticationUser(AuthenticationUser authenticationUser);

    AuthenticationUser getAuthenticationUser();

    /**
     * 清除当前登录信息
     */
    void clear();
}
