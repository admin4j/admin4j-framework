package com.admin4j.framework.security.jwt;

/**
 * 根据用户ID，获取用户信息
 *
 * @author andanyang
 * @since 2023/6/3 22:04
 */
public interface JwtUserDetailsService {

    /**
     * 验证令牌的有效性
     * 通过验证salt的有效性，来达到认证用户令牌的有效性
     * 这里的有效性不是令牌有效时间的有效性，可以在一下几个场景使用：
     * - 1. 用户注销，重置下 salt 使 令牌失效
     * - 2. 多地登陆，控制登录账号数量
     *
     * @param userDetails
     * @param salt
     * @return
     */
    default boolean verifySalt(JwtUserDetails userDetails, String salt) {
        return true;
    }

    /**
     * 通过用户Id获取用户详情
     *
     * @param userId
     * @return
     */
    JwtUserDetails loadUserByUserId(Long userId);


}
