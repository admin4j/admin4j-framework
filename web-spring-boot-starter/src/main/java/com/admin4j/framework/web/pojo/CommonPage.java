package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.CommonResult;
import com.admin4j.common.pojo.ResponseEnum;
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
@AllArgsConstructor
public class CommonPage<T> extends CommonResult<CommonPage.PageResultVO<T>> {

    @ApiModel("分页返回结果")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PageResultVO<T> {
        @ApiModelProperty("当前行数")
        private int size = 0;
        @ApiModelProperty("当前页")
        private int current = 1;
        @ApiModelProperty("总数")
        private int total = 0;
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
