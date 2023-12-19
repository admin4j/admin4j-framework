package com.admin4j.framework.security.authorization;

import java.util.List;

/**
 * 权限uri 服务
 *
 * @author andanyang
 * @since 2023/12/19 14:34
 */
public interface IPermissionUrlService {

    /**
     * 是否忽略 检查权限
     * 例如 admin、管理员可以直接忽略检查拥有全部权限
     *
     * @return
     */
    default boolean ignoreCheck() {
        return false;
    }
    
    /**
     * 获取 系统 所有的 PermissionUri
     *
     * @return
     */
    List<HttpUrlPermission> allPermissionUrl();

    /**
     * 当前用户拥有的权限
     *
     * @return
     */
    List<HttpUrlPermission> getMyPermissionUrls();


}
