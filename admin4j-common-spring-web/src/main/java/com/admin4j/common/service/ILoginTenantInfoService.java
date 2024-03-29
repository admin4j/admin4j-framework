package com.admin4j.common.service;

/**
 * 登录用户认证
 *
 * @author andanyang
 * @since 2023/2/21 11:21
 */
public interface ILoginTenantInfoService {

    /**
     * 租户Id标识，可以返回用户ID， 或者用户name
     * 返回null 当前表示没有租户
     *
     * @return 当前登录的租户
     */
    Long getTenantId();
}
