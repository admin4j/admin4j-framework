package com.admin4j.oss.impl;

import com.admin4j.oss.OssProperties;
import com.admin4j.oss.OssTemplate;
import com.admin4j.oss.UploadFileService;
import com.admin4j.oss.entity.vo.UploadFileVO;
import com.amazonaws.util.Md5Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/4/14 9:27
 */
@RequiredArgsConstructor
public class SimpleOSSUploadFileService implements UploadFileService {

    private final static DateTimeFormatter FILEPATH_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final OssTemplate ossTemplate;

    private final OssProperties ossProperties;

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径，可以是文件类型
     */
    @Override
    public UploadFileVO upload(String path, MultipartFile file) throws IOException {

        UploadFileVO uploadFileVO = new UploadFileVO();
        uploadFileVO.setOriginalFilename(file.getOriginalFilename());
        uploadFileVO.setSize(file.getSize());
        uploadFileVO.setContentType(file.getContentType());
        uploadFileVO.setCreateTime(LocalDateTime.now());

        //计算文件md5
        String md5 = Md5Utils.md5AsBase64(file.getBytes());
        uploadFileVO.setMd5(md5);

        //生成文件存储路径
        if (StringUtils.isNotBlank(path)) {
            uploadFileVO.setPrefix(path);
        }
        path = generateFilePath(uploadFileVO);
        uploadFileVO.setKey(path);

        if (!beforeUpload(uploadFileVO)) {
            return uploadFileVO;
        }

        ossTemplate.putObject(defaultBucketName(), path, file.getInputStream());

        uploadFileVO.setPreviewUrl(getPreviewUrl(path));

        afterUpload(uploadFileVO);

        return uploadFileVO;
    }


    /**
     * 文件预览路径
     *
     * @param key oss key
     * @return 文件阅览路径
     */
    @Override
    public String getPreviewUrl(String key) {
        if (StringUtils.isNotBlank(ossProperties.getPreviewUrl())) {
            return ossProperties.getPreviewUrl() + key;
        }
        return getOssPreviewUrl(key);
    }

    protected String getOssPreviewUrl(String key) {
        return ossTemplate.getObjectURL(defaultBucketName(), key, 3, TimeUnit.HOURS);
    }

    /**
     * 删除文件
     *
     * @param key 文件可以
     */
    @Override
    public void delete(String key) {
        ossTemplate.removeObject(defaultBucketName(), key);
    }

    /**
     * 生成文件路径
     *
     * @param uploadFileVO
     * @return 生成oss key
     */
    protected String generateFilePath(UploadFileVO uploadFileVO) {

        //后缀
        String postfix = null;
        if (StringUtils.isNotBlank(uploadFileVO.getOriginalFilename()) && StringUtils.contains(uploadFileVO.getOriginalFilename(), ".")) {

            postfix = StringUtils.substringAfterLast(uploadFileVO.getOriginalFilename(), ".");
        }

        StringBuilder filePathBuilder = new StringBuilder();

        //文件前缀
        if (StringUtils.isNotBlank(uploadFileVO.getPrefix())) {
            filePathBuilder.append(uploadFileVO.getPrefix()).append('/');
        }

        filePathBuilder.append(uploadFileVO.getCreateTime().format(FILEPATH_DATETIME_FORMATTER))
                .append('/').append(uploadFileVO.getMd5());
        if (postfix != null) {
            filePathBuilder.append(".").append(postfix);
        }
        return filePathBuilder.toString();
    }


    /**
     * @return 默认的桶名称
     */
    protected String defaultBucketName() {
        return ossProperties.getBucketName();
    }

    /**
     * 上传前钩子
     *
     * @param uploadFileVO 上传文件信息
     * @return true 可以上传 false 不用上传（比如根据md5/path 检查文件已存在）
     */
    protected boolean beforeUpload(UploadFileVO uploadFileVO) {

        return true;
    }

    /**
     * 上传完成钩子，可以保存上传记录等
     *
     * @param uploadFileVO
     */
    protected void afterUpload(UploadFileVO uploadFileVO) {
    }
}
