package com.admin4j.framework.security.multi;

import com.admin4j.common.Prioritized;
import com.admin4j.framework.security.properties.FormLoginProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 检查账号密码是否正确
 *
 * @author andanyang
 * @since 2023/11/10 15:53
 */
@Slf4j
@RequiredArgsConstructor
public abstract class MultiCheckUsernamePasswordService implements MultiUserDetailsService {

    protected final PasswordEncoder passwordEncoder;
    protected final FormLoginProperties formLoginProperties;
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
     * 加载用户前的认证。
     * - 比如认证短信验证码是否正确等
     * - 接口限速
     * 等
     *
     * @param multiAuthenticationToken
     * @return 返回是否认证成功
     */
    @Override
    public boolean preVerify(MultiAuthenticationToken multiAuthenticationToken) {
        return true;
    }

    @Override
    public void postLoadUser(MultiAuthenticationToken multiAuthenticationToken) {
        UserDetails principal = (UserDetails) multiAuthenticationToken.getPrincipal();

        // 认证密码是否正确
        if (!passwordEncoder.matches(multiAuthenticationToken.getAuthParameter(formLoginProperties.getPasswordParameter()), principal.getPassword())) {
            log.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }
}
