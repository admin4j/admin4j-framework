package com.admin4j.oss;

import com.admin4j.oss.entity.vo.UploadFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * 文件阅览路径
     *
     * @param key oss key
     * @return 文件阅览路径
     */
    String getPreviewUrl(String key);

    /**
     * 删除文件
     *
     * @param key 文件可以
     */
    void delete(String key);
}
