package com.admin4j.common.service;

/**
 * 用户配置信息
 *
 * @author andanyang
 * @since 2024/3/27 19:06
 */

public interface IUserConfigInfoService {

    /**
     * 通过配置code 获取配置值
     *
     * @param configCode
     * @return
     */
    String getConfig(String configCode);
}
