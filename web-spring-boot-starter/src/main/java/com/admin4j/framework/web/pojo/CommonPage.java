package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author andanyang
 * @since 2022/8/4 13:35
 */
@ApiModel("分页请求返回类")
@Data
public class CommonPage<T> extends SimpleResponse<CommonPage.PageResultVO<T>> {

    public CommonPage() {
        super();
        this.code = ResponseEnum.SUCCESS.getCode();
        this.msg = ResponseEnum.SUCCESS.getMsg();
    }

    public CommonPage(long size, long current, long total, List<T> records, String msg) {

        this();
        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setSize(size);
        pageResultVO.setCurrent(current);
        pageResultVO.setTotal(total);
        pageResultVO.setRows(records);
        data = pageResultVO;
        setMsg(msg);
    }


    public static <T> CommonPage<T> ok(PageResultVO<T> pageResultVO) {
        CommonPage<T> commonPage = new CommonPage<>();
        commonPage.setData(pageResultVO);
        return commonPage;
    }

    public static <T> CommonPage<T> ok(IPage<T> page) {

        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setSize(page.getSize());
        pageResultVO.setCurrent(page.getCurrent());
        pageResultVO.setTotal(page.getTotal());
        pageResultVO.setRows(page.getRecords());

        return ok(pageResultVO);
    }

    //public static <T> CommonPage<T> page(Page<T> page) {
    //
    //    PageResultVO<T> pageResultVO = new PageResultVO<>();
    //    pageResultVO.setSize(page.getPageSize());
    //    pageResultVO.setCurrent(page.getPageNum());
    //    pageResultVO.setTotal(page.getTotal());
    //    pageResultVO.setRows(page.getResult());
    //    return ok(pageResultVO);
    //}

    @ApiModel("分页返回结果")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PageResultVO<T> {
        @ApiModelProperty("当前行数")
        private long size = 0;
        @ApiModelProperty("当前页")
        private long current = 1;
        @ApiModelProperty("总数")
        private long total = 0;
        @ApiModelProperty("当天页数据")
        private List<T> rows;

        private static PageResultVO<?> _EMPTY;

        public static <T> PageResultVO<T> empty() {
            if (_EMPTY == null) {
                _EMPTY = new PageResultVO<>(0, 1, 0, Collections.emptyList());
            }
            return (PageResultVO<T>) _EMPTY;
        }
    }

    /**
     * 默认为空list的
     */
    private static CommonPage<?> _EMPTY;

    public static <T> CommonPage<T> empty() {
        if (_EMPTY == null) {
            _EMPTY = new CommonPage<>();
            _EMPTY.setResponse(ResponseEnum.SUCCESS);
            _EMPTY.setData(PageResultVO.empty());
        }
        return (CommonPage<T>) _EMPTY;
    }

}
