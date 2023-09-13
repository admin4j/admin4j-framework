package com.admin4j.framework.mp.config;

import com.admin4j.common.service.ILoginUserInfoService;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * 自动填充功能
 *
 * @author andanyang
 */
//@RequiredArgsConstructor
public class FieldFillMetaObjectHandler implements MetaObjectHandler {

    private ILoginUserInfoService loginUserInfoService;

    @Autowired(required = false)
    public void setLoginUserInfoService(ILoginUserInfoService loginUserInfoService) {
        this.loginUserInfoService = loginUserInfoService;
    }

    @Override
    public void insertFill(MetaObject metaObject) {

        if (loginUserInfoService != null) {
            this.strictInsertFill(metaObject, "createBy", loginUserInfoService::getUserId, Long.class);
        }
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        if (loginUserInfoService != null) {
            this.strictUpdateFill(metaObject, "updateBy", loginUserInfoService::getUserId, Long.class);
        }
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
