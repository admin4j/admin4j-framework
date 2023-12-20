package com.admin4j.framework.security.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 自定义数据权限(授权)处理
 * 被授 AuthorizationFilter 调用，负责做出最终的访问控制决定
 *
 * @author andanyang
 * @since 2023/12/19 9:53
 */
@RequiredArgsConstructor
public class PermissionAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    /**
     * 有权限
     */
    protected static final AuthorizationDecision GRANTED = new AuthorizationDecision(true);
    /**
     * 没有权限
     */
    protected static final AuthorizationDecision UN_AUTHORIZED = new AuthorizationDecision(false);

    protected final IPermissionUrlService permissionUriService;

    protected AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * Determines if access is granted for a specific authentication and object.
     *
     * @param authentication the {@link Supplier} of the {@link Authentication} to check 当前登录人
     * @param object         the {@link T} object to check
     * @return an {@link AuthorizationDecision} or null if no decision could be made
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        // 是否允许匿名访问
        if (!canAnonymousAccess()) {
            Authentication authenticationGet = authentication.get();
            if (authenticationGet instanceof AnonymousAuthenticationToken) {
                // 匿名访问
                return UN_AUTHORIZED;
            }
        }

        if (ignoreCheck()) {
            return GRANTED;
        }

        // 获取当前请求的 URL 地址
        String requestURI = object.getRequest().getRequestURI();
        String method = object.getRequest().getMethod();
        boolean matchPermission = matchPermission(requestURI, method);
        if (matchPermission) {
            return GRANTED;
        }

        // 沒有匹配到, 查看当前 requestURI 是否需要权限控制
        return urlNeedPermission(requestURI, method) ? UN_AUTHORIZED : GRANTED;
    }

    /**
     * 是否允许匿名访问。
     * 默认不允许。
     *
     * @return
     */
    protected boolean canAnonymousAccess() {
        return permissionUriService.canAnonymousAccess();
    }

    /**
     * 是否忽略 检查权限
     * 例如 admin、管理员可以直接忽略检查拥有全部权限
     *
     * @return
     */
    protected boolean ignoreCheck() {

        return permissionUriService.ignoreCheck();
    }

    /**
     * url 是否需要授权
     * TODO 放在 service 立马
     *
     * @return
     */
    public boolean urlNeedPermission(String requestURI, String method) {

        Collection<HttpUrlPermission> allPermissionUrls = getAllPermissionUrls();
        for (HttpUrlPermission urlPermission : allPermissionUrls) {
            // method 相同 && 请求路径可以匹配
            if (
                    (urlPermission.getHttpMethod() == null || urlPermission.getHttpMethod().name().equalsIgnoreCase(method))
                            &&
                            antPathMatcher.match(urlPermission.getRequestURI(), requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前用户是否可以匹配到访问该url权限
     *
     * @param requestURI
     * @return
     */
    public boolean matchPermission(String requestURI, String method) {
        Collection<HttpUrlPermission> permissionUrls = getPermissionUrls();

        if (permissionUrls == null || permissionUrls.isEmpty()) {
            return false;
        }

        for (HttpUrlPermission urlPermission : permissionUrls) {

            // method 相同 && 请求路径可以匹配
            if (
                    (urlPermission.getHttpMethod() == null || urlPermission.getHttpMethod().name().equalsIgnoreCase(method))
                            &&
                            antPathMatcher.match(urlPermission.getRequestURI(), requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前用户拥有的权限
     *
     * @return
     */
    public Collection<HttpUrlPermission> getPermissionUrls() {

        return permissionUriService.getMyPermissionUrls();
    }


    /**
     * 获取全部权限
     *
     * @return
     */
    protected Collection<HttpUrlPermission> getAllPermissionUrls() {
        return permissionUriService.allPermissionUrl();
    }
}
