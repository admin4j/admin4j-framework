package com.admin4j.common.service.impl;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.service.IUserContextHolder;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 简单的实现
 * 当前登录用户上下文信息，可实现切换用户，切换租户。
 *
 * @author andanyang
 * @since 2021/7/27 10:56
 */
public class SimpleUserContextHolder implements IUserContextHolder {

    /**
     * 支持父子线程之间的数据传递 THREAD_LOCAL_TENANT
     */
    protected final ThreadLocal<AuthenticationUser> THREAD_LOCAL_USER;

    public SimpleUserContextHolder() {
        THREAD_LOCAL_USER = new ThreadLocal<>();
    }

    public SimpleUserContextHolder(ThreadLocal<AuthenticationUser> threadLocal) {
        THREAD_LOCAL_USER = threadLocal;
    }

    /**
     * 当前会话注销登录
     */
    @Override
    public void loginOut() {
        clear();
    }

    @Override
    public AuthenticationUser getAuthenticationUser() {
        return THREAD_LOCAL_USER.get();
    }

    /**
     * 设置登录者信息
     *
     * @param authenticationUser 认证用户
     */
    @Override
    public void setAuthenticationUser(AuthenticationUser authenticationUser) {
        THREAD_LOCAL_USER.set(authenticationUser);
    }

    /**
     * 获取用户
     *
     * @return String
     */
    public AuthenticationUser getLoginUser() {
        AuthenticationUser authenticationUser = THREAD_LOCAL_USER.get();

        ResponseEnum.FAIL_AUTH_FORBIDDEN.notNull(authenticationUser);

        return authenticationUser;
    }

    /**
     * 获取用户
     *
     * @return String
     */
    @Override
    public boolean isLogin() {
        AuthenticationUser authenticationUser = THREAD_LOCAL_USER.get();
        return ObjectUtils.isNotEmpty(authenticationUser);
    }

    /**
     * 获取用户
     *
     * @return String
     */
    public AuthenticationUser getLoginUserNoCheck() {
        return THREAD_LOCAL_USER.get();
    }

    /**
     * 清除LOCAL
     */
    @Override
    public void clear() {
        THREAD_LOCAL_USER.remove();
    }

    @Override
    public void offTenant() {
        setTenantId(0L);
    }

    /**
     * get租户
     */
    @Override
    public Long getTenantId() {
        AuthenticationUser loginUserNoCheck = getLoginUserNoCheck();
        // 小心三目表达式，NPE
        if (loginUserNoCheck == null) {
            return null;
        } else {
            return loginUserNoCheck.getTenantId();
        }
    }

    /**
     * 设置租户
     *
     * @param tenant
     */
    @Override
    public void setTenantId(Long tenant) {
        getLoginUser().setTenantId(tenant);
    }

    /**
     * get用户ID
     */
    @Override
    public Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    @Override
    public void setUserId(Long userId) {
        // 如果需要切换其他资料信息字类需要实现 切换用户信息方法
        getLoginUser().setUserId(userId);
    }
}
