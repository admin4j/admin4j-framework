package com.admin4j.framework.security.mult;

import com.admin4j.common.Prioritized;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 多渠道登录的获取用户
 *
 * @author andanyang
 * @since 2023/6/1 16:27
 */
public interface MultiUserDetailsService extends Prioritized {

    /**
     * 该类是否支持指定的authType登录方式
     *
     * @param authType 指定的authType登录方式
     * @return
     */
    String support();


    /**
     * 加载用户前的认证。比如认证短信验证码是否正确等
     *
     * @return 返回是否认证成功
     */
    boolean preVerify(MultiAuthenticationToken multiAuthenticationToken);

    /**
     * 多渠道登录加载用户
     *
     * @param multiToken 多渠道登录key，可以是手机号、邮箱号，openid等
     * @return 具体用户信息
     */
    UserDetails loadUserByMultiToken(String multiToken);

    /**
     * 加载用户后的回调。比如可以密码是否正确等
     *
     * @param multiAuthenticationToken 最终认证成功的 AuthenticationToken
     */
    default void postLoadUser(MultiAuthenticationToken multiAuthenticationToken) {
    }
}
