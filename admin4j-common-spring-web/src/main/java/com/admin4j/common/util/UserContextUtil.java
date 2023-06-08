package com.admin4j.common.util;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.service.IUserContextHolder;

/**
 * 当前登录状态工具类
 *
 * @author andanyang
 * @since 2023/6/7 8:48
 */
public class UserContextUtil {

    public static IUserContextHolder userContextHolder;

    /**
     * 用户Id标识，可以返回用户ID， 或者用户name
     *
     * @return 当前登录的用户ID
     */
    public static Long getUserId() {
        return userContextHolder.getUserId();
    }

    /**
     * 设置用户ID
     */
    public static void setUserId(Long userId) {
        userContextHolder.setUserId(userId);
    }

    /**
     * 是否登录
     *
     * @return boolean 是否登录
     */
    public static boolean isLogin() {
        return userContextHolder.isLogin();
    }

    /**
     * 当前会话注销登录
     */
    public static void loginOut() {
        userContextHolder.loginOut();
    }


    /**
     * 设置租户
     */

    public static void setTenant(Long tenant) {
        userContextHolder.setTenantId(tenant);
    }

    /**
     * 租户Id标识，可以返回用户ID， 或者用户name
     * 返回null 当前表示没有租户
     *
     * @return 当前登录的租户
     */
    public static Long getTenant() {
        return userContextHolder.getTenantId();
    }

    public static AuthenticationUser getUser() {
        return userContextHolder.getAuthenticationUser();
    }

    public static void setUser(AuthenticationUser authenticationUser) {
        userContextHolder.setAuthenticationUser(authenticationUser);
    }

    public static boolean isAdmin() {
        return userContextHolder.getAuthenticationUser().isAdmin();
    }


    public static void clear() {
        userContextHolder.clear();
    }
}
