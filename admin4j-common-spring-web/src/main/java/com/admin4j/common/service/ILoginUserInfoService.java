package com.admin4j.common.service;

/**
 * 登录用户认证
 *
 * @author andanyang
 * @since 2023/2/21 11:21
 */
public interface ILoginUserInfoService {

    /**
     * 用户Id标识，可以返回用户ID， 或者用户name
     *
     * @return 当前登录的用户ID
     */
    Long getUserId();


}
