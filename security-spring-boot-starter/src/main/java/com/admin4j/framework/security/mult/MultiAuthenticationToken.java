package com.admin4j.framework.security.mult;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * 多渠道登录 token 令牌
 *
 * @author andanyang
 * @since 2023/6/1 15:29
 */
public class MultiAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1462643243455662933L;
    /**
     * 登录类型
     */
    @Getter
    private String authType;
    /**
     * 登录用户名，可以是手机号，邮箱，openid等
     */
    @Getter
    private Object principal;
    /**
     * 登录其他参数
     */
    @Getter
    @Setter
    private Map<String, String[]> authParameters;

    /**
     * 初始化已认证的数据
     *
     * @param principal   认证之后存的是用户信息
     * @param authType
     * @param authorities
     */
    public MultiAuthenticationToken(String authType, Object principal,
                                    Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.authType = authType;
        super.setAuthenticated(true); // must use super, as we override
    }

    /**
     * 初始化未认证数据
     *
     * @param authType       登录类型
     * @param principal      在认证之前 principal 存的登录用户名，可以是手机号，邮箱，openid等
     * @param authParameters 登录其他参数
     */
    public MultiAuthenticationToken(String authType, Object principal, Map<String, String[]> authParameters) {
        super(null);
        this.principal = principal;
        this.authType = authType;
        this.authParameters = authParameters;
        setAuthenticated(false);
    }

    /**
     * 初始化未认证数据
     *
     * @param authType       登录类型
     * @param principal      登录用户名，可以是手机号，邮箱，openid等
     * @param authParameters 登录其他参数
     */
    public static MultiAuthenticationToken unauthenticated(String authType, Object principal, Map<String, String[]> authParameters) {
        return new MultiAuthenticationToken(authType, principal, authParameters);
    }

    public String getAuthParameter(String parameter) {
        String[] values = this.authParameters == null ? null : this.authParameters.get(parameter);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
