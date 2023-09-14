package com.admin4j.oss;

import com.admin4j.oss.entity.vo.UploadFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件
 *
 * @author andanyang
 * @since 2023/4/14 9:14
 */
public interface UploadFileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径
     */
    UploadFileVO upload(String path, MultipartFile file) throws IOException;

    /**
     * 上传文件
     *
     * @param key 指定路径
     * @param is  InputStream
     */
    UploadFileVO upload(String key, InputStream is) throws IOException;

    /**
     * 文件阅览路径
     *
     * @param key oss key
     * @return 文件阅览路径
     */
    String getPreviewUrl(String key);

    /**
     * 文件内网阅览路径
     *
     * @param key oss key
     * @return 文件阅览路径
     */
    String getPreviewIntranetUrl(String key);

    /**
     * 通过OSS直接查看文件预览路径
     * 获取私有链接
     *
     * @param key     oss key
     * @param expires 私有链接有效秒数
     * @return 文件阅览路径
     */
    String getPrivateUrl(String key, Integer expires);

    /**
     * 删除文件
     *
     * @param key 文件可以
     */
    void delete(String key);
}
