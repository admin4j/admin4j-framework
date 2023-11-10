package com.admin4j.framework.security.mult;

import com.admin4j.framework.security.properties.FormLoginProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 默认的账号密码form登录用户加载类
 *
 * @author andanyang
 * @since 2023/6/2 13:43
 */
@Slf4j
public class UsernamePasswordUserDetailsService extends MultiCheckUsernamePasswordService {


    private final UserDetailsService userDetailsService;

    public UsernamePasswordUserDetailsService(PasswordEncoder passwordEncoder, FormLoginProperties formLoginProperties, UserDetailsService userDetailsService) {
        super(passwordEncoder, formLoginProperties);
        this.userDetailsService = userDetailsService;
    }


    /**
     * 多渠道登录加载用户
     *
     * @param multiToken 多渠道登录key，可以是手机号、邮箱号，openid等
     * @return 具体用户信息
     */
    @Override
    public UserDetails loadUserByMultiToken(String multiToken) {
        return userDetailsService.loadUserByUsername(multiToken);
    }


}
