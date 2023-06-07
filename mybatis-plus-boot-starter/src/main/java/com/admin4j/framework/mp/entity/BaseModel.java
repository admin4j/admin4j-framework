package com.admin4j.framework.mp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @author andanyang
 * @since 2023/6/6 15:34
 */
public class BaseModel {

    @ApiModelProperty(value = "是否删除(0-存在，1-不存在)")
    @TableLogic
    private Boolean delFlag;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    private Long updateBy;


    private Long tenant;

    /**
     * 填充数据
     */
    //public void fill() {
    //    tenantId = IUserContextHolder.getTenant();
    //    createTime = LocalDateTime.now();
    //    updateTime = createTime;
    //    createBy = IUserContextHolder.getUserId();
    //    updateBy = IUserContextHolder.getUserId();
    //}

    /**
     * 清除基础信息
     */
    public void clearBaseInfo() {

        tenant = null;
        createTime = null;
        updateTime = null;
        createBy = null;
        updateBy = null;
        delFlag = null;
    }
}
