package com.admin4j.framework.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author andanyang
 * @since 2023/6/3 22:04
 */
public interface JwtUserDetailsService {

    /**
     * 通过用户Id获取用户详情
     *
     * @param userId
     * @return
     */
    UserDetails loadUserByUserId(Long userId);
}
