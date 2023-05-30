package com.admin4j.framework.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * @author andanyang
 * @since 2023/5/30 14:51
 */
@Data
public class JWTUserDetails implements UserDetails {


    private static final long serialVersionUID = -5943535608623539244L;
    private String password;
    private String username;
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    private String jwtSalt;
    private String userId;

}
