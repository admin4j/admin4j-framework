package com.admin4j.framework.excel;

import com.admin4j.framework.excel.listener.ExcelListener;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * Excel 工具类
 * https://easyexcel.opensource.alibaba.com/docs/current/
 *
 * @author andanyang
 */
public class ExcelUtils {

    //static ObjectProvider<ExcelReadLifecycle> excelReadLifecycles;
    @Setter
    static ExcelWriteLifecycle excelWriteLifecycle;

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param outputStream 响应
     * @param sheetName    Excel sheet 名
     * @param aClass       Excel aClass 头
     * @param data         数据列表哦
     * @param <T>          泛型，保证 aClass 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(OutputStream outputStream, String sheetName,
                                 List<T> data, Class<T> aClass, boolean autoCloseStream) {

        if (excelWriteLifecycle != null) {

            excelWriteLifecycle.before(data, aClass);
        }

        // 输出 Excel
        ExcelWriterSheetBuilder sheet = EasyExcelFactory.write(outputStream, aClass)
                .autoCloseStream(autoCloseStream)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .sheet(sheetName);


        try {
            sheet.doWrite(data);
        } finally {
            if (excelWriteLifecycle != null) {

                excelWriteLifecycle.after(data, aClass);
            }
        }
    }

    /**
     * 写入 response 返回
     *
     * @param filename
     * @param data
     * @param aClass
     * @param <T>
     * @throws IOException
     */
    public static <T> void write2Http(String filename, List<T> data, Class<T> aClass) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            throw new RuntimeException("当前非web环境");
        }
        HttpServletResponse response = requestAttributes.getResponse();

        assert response != null;

        if (!StringUtils.contains(filename, ".")) {
            filename = filename + ".xlsx";
        }
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8").replace("\\+", "%20"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        //autoCloseStream 不要自动关闭，交给 Servlet 自己处理
        write(response.getOutputStream(), null, data, aClass, false);
    }

    public static <T> List<T> read(MultipartFile file, Class<T> clazz) throws IOException {
        ExcelListener<T> readListener = new ExcelListener<>();
        EasyExcelFactory.read(file.getInputStream(), clazz, readListener)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAll();

        return readListener.getData();
    }

    /**
     * 读取excel
     *
     * @param file         excel 文件
     * @param clazz        返回 数据clazz
     * @param readListener 监听器
     * @param <T>          返回 数据clazz
     * @return 解析结构
     * @throws IOException 读取失败的情况
     */
    public static <T> void read(MultipartFile file, Class<T> clazz, ReadListener<T> readListener) throws IOException {
        EasyExcelFactory.read(file.getInputStream(), clazz, readListener)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAll();
    }

}
