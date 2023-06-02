package com.admin4j.framework.security.mult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/6/1 16:23
 */
@RequiredArgsConstructor
@Slf4j
public class MultiAuthenticationProvider implements AuthenticationProvider {

    private final List<MultiUserDetailsService> userDetailServices;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

    /**
     * 支持处理的 Token 类型为 MultiAuthenticationToken
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return MultiAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 编写具体的身份认证逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MultiAuthenticationToken authenticationToken = (MultiAuthenticationToken) authentication;

        MultiUserDetailsService userDetailService = null;
        if (userDetailServices != null) {
            for (MultiUserDetailsService item : userDetailServices) {
                if (item.support(authenticationToken.getAuthType())) {
                    userDetailService = item;
                    break;
                }
            }
        }

        if (userDetailService == null) {
            throw new InternalAuthenticationServiceException("This authentication authType is not supported");
        }

        boolean b = userDetailService.preVerify(authenticationToken);
        if (!b) {
            throw new InternalAuthenticationServiceException("Authentication failure");
        }

        UserDetails userDetails = userDetailService.loadUserByMultiToken((String) authenticationToken.getPrincipal());
        this.preAuthenticationChecks.check(userDetails);

        //生成一个认证成功 Authentication
        MultiAuthenticationToken multiAuthenticationToken = new MultiAuthenticationToken(authenticationToken.getAuthType(), userDetails, userDetails.getAuthorities());
        multiAuthenticationToken.setDetails(authenticationToken.getDetails());
        multiAuthenticationToken.setAuthParameters(authenticationToken.getAuthParameters());

        userDetailService.postLoadUser(multiAuthenticationToken);

        this.postAuthenticationChecks.check(userDetails);
        return multiAuthenticationToken;
    }


    /**
     * UserDetails 检查
     */
    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (user == null) {
                throw new InternalAuthenticationServiceException("The specified user was not found");
            }

            if (!user.isAccountNonLocked()) {
                MultiAuthenticationProvider.log
                        .debug("Failed to authenticate since user account is locked");
                throw new LockedException(MultiAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            }
            if (!user.isEnabled()) {
                MultiAuthenticationProvider.log
                        .debug("Failed to authenticate since user account is disabled");
                throw new DisabledException(MultiAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            }
            if (!user.isAccountNonExpired()) {
                MultiAuthenticationProvider.log
                        .debug("Failed to authenticate since user account has expired");
                throw new AccountExpiredException(MultiAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }

    }

    /**
     * UserDetails 检查
     */
    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("Failed to authenticate since user account credentials have expired");
                throw new CredentialsExpiredException(MultiAuthenticationProvider.this.messages
                        .getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                                "User credentials have expired"));
            }
        }
    }

}
