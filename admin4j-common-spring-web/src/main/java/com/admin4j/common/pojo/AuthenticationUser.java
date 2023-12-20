package com.admin4j.common.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * UserContext 用户上下文
 *
 * @author andanyang
 * @since 2021/7/30 9:54
 */
@Data
@ApiModel("用戶登录信息")
public class AuthenticationUser implements Serializable {

    private static final long serialVersionUID = -1464440942632391300L;

    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("是否是超级管理员")
    private boolean admin;


    /**
     * 权限列表
     */
    @ApiModelProperty("权限code列表")
    private Collection<String> permissions;

    // private String fromService;

    private String referer;


    /**
     * 获取原始认证对消
     *
     * @return
     */
    private Object originAuthentication;
}
