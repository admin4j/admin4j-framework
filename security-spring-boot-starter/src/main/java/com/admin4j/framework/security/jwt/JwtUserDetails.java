package com.admin4j.framework.security.jwt;

import com.admin4j.framework.security.authorization.PermissionGrantedAuthority;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author andanyang
 * @since 2023/5/30 14:51
 */
public interface JwtUserDetails extends UserDetails {


    // private static final long serialVersionUID = -5943535608623539244L;
    // private String password;
    // private String username;
    // private Set<GrantedAuthority> authorities;
    // private boolean accountNonExpired = true;
    // private boolean accountNonLocked = true;
    // private boolean credentialsNonExpired = true;
    // private boolean enabled = true;

    /**
     * jwt 盐
     */
    String getJwtSalt();

    /**
     * 用户ID
     */
    Long getUserId();

    boolean isAdmin();

    /**
     * 租户
     *
     * @return
     */
    default Long getTenantId() {
        return 0L;
    }

    /**
     * 登录方式
     */
    default String getAuthType() {
        return "";
    }

    /**
     * 权限列表
     */
    Collection<String> getPermissions();

    @JsonIgnore
    @JSONField(serialize = false)
    default Collection<GrantedAuthority> getAuthorities() {
        if (ObjectUtils.isEmpty(getPermissions())) {
            return Collections.emptySet();
        }
        return getPermissions().stream().map(PermissionGrantedAuthority::new).collect(Collectors.toSet());
    }
}
