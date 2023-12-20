package com.admin4j.framework.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限字符串code
 *
 * @author andanyang
 * @since 2023/12/20 10:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -2619854289135953331L;
    // 权限字符串code
    private String permission;

    /**
     * If the <code>GrantedAuthority</code> can be represented as a <code>String</code>
     * and that <code>String</code> is sufficient in precision to be relied upon for an
     * access control decision by an {@link AccessDecisionManager} (or delegate), this
     * method should return such a <code>String</code>.
     * <p>
     * If the <code>GrantedAuthority</code> cannot be expressed with sufficient precision
     * as a <code>String</code>, <code>null</code> should be returned. Returning
     * <code>null</code> will require an <code>AccessDecisionManager</code> (or delegate)
     * to specifically support the <code>GrantedAuthority</code> implementation, so
     * returning <code>null</code> should be avoided unless actually required.
     *
     * @return a representation of the granted authority (or <code>null</code> if the
     * granted authority cannot be expressed as a <code>String</code> with sufficient
     * precision).
     */
    @Override
    public String getAuthority() {
        return permission;
    }
}
