package com.admin4j.framework.mp.config;

import com.admin4j.common.service.ILoginUserInfoService;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 自动填充功能
 *
 * @author andanyang
 */
@RequiredArgsConstructor
public class FieldFillMetaObjectHandler implements MetaObjectHandler {

    private final ILoginUserInfoService loginUserInfoService;

    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, "createBy", loginUserInfoService::getUserId, Long.class);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictUpdateFill(metaObject, "updateBy", loginUserInfoService::getUserId, Long.class);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
