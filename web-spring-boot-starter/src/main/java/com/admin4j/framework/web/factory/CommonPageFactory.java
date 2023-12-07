package com.admin4j.framework.web.factory;

import com.admin4j.framework.web.pojo.CommonPage;
import com.github.pagehelper.Page;
import org.springframework.util.Assert;

import java.util.List;

/**
 * page helper
 *
 * @author andanyang
 * @since 2023/6/9 10:24
 */
public class CommonPageFactory {

    /**
     * @param pageInfo 分页信息
     */
    public static <T> CommonPage<T> ok(Page<T> pageInfo) {

        CommonPage.PageResultVO<T> pageResultVO = new CommonPage.PageResultVO<>();

        pageResultVO.setTotal(pageInfo.getTotal());
        pageResultVO.setCurrent(pageInfo.getPageNum());
        pageResultVO.setSize(pageInfo.size());
        pageResultVO.setRecords(pageInfo);

        return CommonPage.ok(pageResultVO);
    }


    public static <T> CommonPage<T> ok(List<T> list) {

        if (list instanceof Page) {
            return ok((Page<T>) list);
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
    public static <T> CommonPage<T> ok(Page<?> pageInfo, List<T> list) {

        CommonPage.PageResultVO<T> pageResultVO = new CommonPage.PageResultVO<>();

        pageResultVO.setTotal(pageInfo.getTotal());
        pageResultVO.setCurrent(pageInfo.getPageNum());
        pageResultVO.setSize(pageInfo.size());
        pageResultVO.setRecords(list);

        return CommonPage.ok(pageResultVO);
    }

    /**
     * @param pageInfo 分页信息
     * @param list     具体数据
     */
    public static <T> CommonPage<T> ok(List<?> pageInfo, List<T> list) {

        Assert.isTrue(pageInfo instanceof Page, "返回PageInfo 类型错误");
        return ok((Page<?>) pageInfo, list);
    }
}
