package com.admin4j.framework.security.mult;

import com.admin4j.common.Prioritized;
import com.admin4j.framework.security.properties.FormLoginProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
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
@RequiredArgsConstructor
public class UsernamePasswordUserDetailsService implements MultiUserDetailsService {


    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final FormLoginProperties formLoginProperties;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    @Override
    public int getPriority() {
        return Prioritized.MAX_PRIORITY;
    }

    /**
     * 该类是否支持指定的authType登录方式
     *
     * @param authType 指定的authType登录方式
     * @return
     */
    @Override
    public String support() {
        return MultiAuthenticationFilter.DEFAULT_AUTH_TYPE;
    }

    /**
     * 加载用户前的认证。比如认证短信验证码是否正确等
     *
     * @param multiAuthenticationToken
     * @return 返回是否认证成功
     */
    @Override
    public boolean preVerify(MultiAuthenticationToken multiAuthenticationToken) {
        return true;
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

    @Override
    public void postLoadUser(MultiAuthenticationToken multiAuthenticationToken) {
        UserDetails principal = (UserDetails) multiAuthenticationToken.getPrincipal();

        //认证密码是否正确
        if (!passwordEncoder.matches(multiAuthenticationToken.getAuthParameter(formLoginProperties.getPasswordParameter()), principal.getPassword())) {
            log.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }
}
