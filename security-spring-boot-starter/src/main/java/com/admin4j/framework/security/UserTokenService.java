package com.admin4j.framework.security;

import com.admin4j.framework.security.jwt.JwtUserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户令牌服务
 *
 * @author andanyang
 * @since 2023/5/30 13:27
 */

public interface UserTokenService {


    ///**
    // * 创建令牌
    // *
    // * @param claims 用户信息
    // * @return 令牌
    // */
    ////String createToken(Map<String, Object> claims);

    String createToken(JwtUserDetails userDetails);

    /**
     * 获取请求token(令牌)
     *
     * @param request
     * @return token
     */
    String getToken(HttpServletRequest request);

    /**
     * 根据令牌token, 获取登录用户名
     *
     * @param token
     * @return 登录用户名
     */
    JwtUserDetails getUserDetails(String token);
}
