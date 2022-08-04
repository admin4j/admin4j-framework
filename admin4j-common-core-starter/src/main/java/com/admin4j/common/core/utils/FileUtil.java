package com.admin4j.common.core.utils;

import com.admin4j.common.core.exception.BaseException;
import com.admin4j.common.core.exception.UtilException;
import com.admin4j.common.core.utils.text.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * /**
 * file工具类
 *
 * @author: gle
 * @date: 2021-06-02 10:19
 */
@Slf4j
public class FileUtil {

    /**
     * 获取资源文件夹的file
     *
     * @param resources
     * @return
     */
    public static InputStream getFile(String resources) {
        InputStream resourceAsStream = FileUtil.class.getClassLoader().getResourceAsStream(resources);
        return resourceAsStream;
    }


    /**
     * 转换multipartFile 为 file对象
     *
     * @param file
     * @return
     */
    public static File multipartFileToFile(MultipartFile file) {
        File csvFile = null;
        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        try {
            //创建临时文件
            csvFile = File.createTempFile(UUID.fastUUID().toString(), prefix);
            file.transferTo(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFile;
    }

    /**
     * 获取上传文件后缀
     *
     * @param file
     * @return
     */
    public static String getSuffix(MultipartFile file) {
        if (file == null) {
            throw new BaseException("上传的文件为空！");
        }
        String filename = file.getResource().getFilename();
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getPath() {
        return FileUtil.class.getResource("/").getPath();
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readUserHomeFile(String pathName) {
        return new File(System.getProperty("user.home") + File.separator + pathName);
    }

    /**
     * 下载excel文件模板
     */
    public static void downloadTemplate(HttpServletResponse response, InputStream inputStream, String name) {
        try {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name, "utf-8"));
            response.setHeader("Access-Control-Expose-Headers", "FileName");
            response.setHeader("FileName", URLEncoder.encode(name, "UTF-8"));
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) >= 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            UtilException.throwException(e);
        }
    }
}
