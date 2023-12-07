package com.admin4j.framework.web.factory;

import com.admin4j.framework.web.pojo.CommonPage;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 适用于mybatis-plus 的分页工厂
 *
 * @author andanyang
 * @since 2023/6/9 10:24
 */
public class CommonPageMpFactory {

    public static <T> CommonPage<T> ok(IPage<T> pageInfo) {

        CommonPage.PageResultVO<T> pageResultVO = new CommonPage.PageResultVO<>();

        pageResultVO.setTotal(pageInfo.getTotal());
        pageResultVO.setCurrent(pageInfo.getCurrent());
        pageResultVO.setSize(pageInfo.getRecords() == null ? 0 : pageInfo.getRecords().size());
        pageResultVO.setRecords(pageInfo.getRecords());

        return CommonPage.ok(pageResultVO);
    }

    public static <T> CommonPage<T> ok(List<T> list) {

        if (list instanceof IPage) {
            return ok((IPage<T>) list);
        }

        CommonPage.PageResultVO<T> pageResultVO = new CommonPage.PageResultVO<>();

        pageResultVO.setTotal(list.size());
        pageResultVO.setCurrent(1);
        pageResultVO.setSize(list.size());
        pageResultVO.setRecords(list);

        return CommonPage.ok(pageResultVO);
    }

    /**
     * @param pageInfo 分页信息
     * @param list     具体数据
     */
    public static <T> CommonPage<T> ok(IPage<?> pageInfo, List<T> list) {

        CommonPage.PageResultVO<T> pageResultVO = new CommonPage.PageResultVO<>();

        pageResultVO.setTotal(pageInfo.getTotal());
        pageResultVO.setCurrent(pageInfo.getCurrent());
        pageResultVO.setSize(pageInfo.getRecords() == null ? 0 : pageInfo.getRecords().size());
        pageResultVO.setRecords(list);

        return CommonPage.ok(pageResultVO);
    }
}
