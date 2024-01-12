package com.admin4j.framework.web.pojo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 分页查询茶树
 *
 * @author andanyang
 * @since 2023/3/13 9:19
 */
@NoArgsConstructor
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 7145281479131775043L;
    /**
     * 当前页
     */
    @ApiModelProperty("当前页,从1开始")
    @Getter
    @Setter
    private Integer pageNum = 1;
    /**
     * 一页显示几条
     */
    @ApiModelProperty("一页显示几条")
    @Getter
    @Setter
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    @Getter
    @Setter
    private String orderByColumn;
    /**
     * 排序方式
     */
    @ApiModelProperty("排序方式,默认倒叙")
    @Getter
    @Setter
    private boolean isAsc = false;

    @ApiModelProperty(value = "是否需要分页", hidden = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private boolean needPage = true;

    @ApiModelProperty("是否显示分页")
    @Getter
    @Setter
    private boolean needCount = true;

    public PageQuery(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 获取默认的 排序字段
     * 由子类实现
     *
     * @return
     */
    protected String getDefaultOrderByColumn() {
        return null;
    }

    public String getOrderByColumn() {
        return StringUtils.defaultString(orderByColumn, getDefaultOrderByColumn());
    }

    /**
     * 关闭分页
     */
    public void closePage() {
        needPage = false;
        needCount = false;
    }

    /**
     * 开始分页
     */
    public void startPage() {
        if (needPage && this.pageNum != null && this.pageSize != null) {
            String orderBy = this.getOrderByColumn();
            if (StringUtils.isNotEmpty(orderBy)) {
                orderBy = orderBy + (isAsc ? " asc" : " desc");
            }
            Page<Object> pageHelper = PageHelper.startPage(pageNum, pageSize, orderBy);
            pageHelper.setCount(needCount);
        }
    }

    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
