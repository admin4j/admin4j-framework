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
     * 上传文件,指定路径
     *
     * @param file 文件
     * @param path 路径
     */
    UploadFileVO uploadByPath(String path, MultipartFile file) throws IOException;

    /**
     * 上传文件,指定路径
     *
     * @param is   文件流
     * @param path 路径
     */

    default UploadFileVO uploadByPath(String path, InputStream is) throws IOException {
        return uploadByPath(path, null, null, is);
    }

    /**
     * 上传文件
     *
     * @param path             指定文件路径
     * @param originalFilename 文件原始名称
     * @param contentType      文件类型
     * @param is               上传流
     * @return
     * @throws IOException
     */
    UploadFileVO uploadByPath(String path, String originalFilename, String contentType, InputStream is) throws IOException;

    /**
     * 上传文件，指定文件key
     *
     * @param key 指定文件key
     * @param is  InputStream
     */
    default UploadFileVO uploadByKey(String key, InputStream is) throws IOException {
        return uploadByKey(key, null, null, is);
    }

    /**
     * 上传文件
     *
     * @param key              指定文件key
     * @param originalFilename 文件原始名称
     * @param contentType      文件类型
     * @param is               上传流
     * @return
     * @throws IOException
     */
    UploadFileVO uploadByKey(String key, String originalFilename, String contentType, InputStream is) throws IOException;

    /**
     * 上传文件
     *
     * @param uploadFileVO 文件描述,
     * @param is
     * @return
     * @throws IOException
     */
    UploadFileVO upload(UploadFileVO uploadFileVO, InputStream is) throws IOException;

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
