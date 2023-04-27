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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * easyExcel 导出导入时间转换类
 * <p>
 * *    @ExcelProperty(converter = LocalDateTimeConverter.class) 使用转换器
 * <p>
 * *    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
 * *    private  String date;
 * <p>
 * <p>
 * 接收百分比的数字
 * *    @NumberFormat("#.##%")
 * *    private String doubleData;
 *
 * @author: andanyang
 * @date: 2021-07-07 9:53
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    static final BigDecimal ONE_DAY_SECOND = BigDecimal.valueOf(24 * 60 * 60);
    final static DateTimeFormatter NORM_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        if (cellData.getType().equals(CellDataTypeEnum.NUMBER)) {

            BigDecimal numberValue = cellData.getNumberValue();

            numberValue = numberValue.multiply(ONE_DAY_SECOND);
            numberValue = numberValue.setScale(0, RoundingMode.HALF_UP);
            Instant instant = Instant.ofEpochSecond(numberValue.longValue());
            return LocalDateTime.ofInstant(instant, ZoneId.of("UTC")).minusYears(70).minusDays(1);
        }

        return LocalDateTime.parse(cellData.getStringValue(), NORM_DATETIME_FORMAT);
    }


    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDateTime> context) throws Exception {
        return new WriteCellData<>(context.getValue().format(NORM_DATETIME_FORMAT));
    }

}
