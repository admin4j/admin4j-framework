package com.admin4j.framework.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

/**
 * http 请求权限数据
 *
 * @author andanyang
 * @since 2023/12/19 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpUrlPermission {

    /**
     * http 请求方法;
     * 为null 表示不限制请求方法
     */
    private HttpMethod httpMethod;
    /**
     * http 请求地址
     * 如： /user/1
     */
    private String requestURI;
}
