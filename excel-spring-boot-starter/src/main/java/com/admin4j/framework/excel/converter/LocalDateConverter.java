package com.admin4j.framework.excel.converter;/**
 * @author: XXX
 * @date: 2021-07-07 9:53
 */

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * easyExcel 导出导入时间转换类
 *
 * @author: andanyang
 * @date: 2021-07-07 9:53
 */
public class LocalDateConverter implements Converter<LocalDate> {
    private final static DateTimeFormatter NORM_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Class<LocalDate> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        if (cellData.getType().equals(CellDataTypeEnum.NUMBER)) {
            LocalDate localDate = LocalDate.of(1900, 1, 1);
            //excel 有些奇怪的bug, 导致日期数差2
            localDate = localDate.plusDays(cellData.getNumberValue().longValue() - 2);
            return localDate;
        } else {
            return LocalDate.parse(cellData.getStringValue(), NORM_DATE_FORMAT);
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDate> context) throws Exception {
        return new WriteCellData<>(context.getValue().format(NORM_DATE_FORMAT));
    }
}
