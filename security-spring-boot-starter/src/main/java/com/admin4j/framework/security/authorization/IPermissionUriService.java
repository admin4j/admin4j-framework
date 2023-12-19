package com.admin4j.framework.security.authorization;

import java.util.List;

/**
 * 权限uri 服务
 *
 * @author andanyang
 * @since 2023/12/19 14:34
 */
public interface IPermissionUriService {

    /**
     * 获取 系统 所有的 PermissionUri
     *
     * @return
     */
    List<String> allPermissionUri();

    /**
     * 当前用户拥有的权限
     *
     * @return
     */
    List<String> getMyPermissionUrls();
}
