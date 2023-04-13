package com.admin4j.oss;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * oss操作模板
 *
 * @author andanyang
 * @since 2023/4/13 15:20
 */
public interface OssTemplate {
    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 获取所有的bucket
     *
     * @return Bucket list
     */
    List<Bucket> getAllBuckets();

    /**
     * 通过bucket名称删除bucket
     *
     * @param bucketName
     */
    void removeBucket(String bucketName);

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param contextType 文件类型
     * @throws Exception
     */
    PutObjectResult putObject(String bucketName, String objectName, InputStream stream, String contextType) throws IOException;

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception
     */
    PutObjectResult putObject(String bucketName, String objectName, InputStream stream) throws IOException;

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return S3Object
     */
    S3Object getObject(String bucketName, String objectName);

    /**
     * 获取对象的url
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return 对象的url
     */
    String getObjectURL(String bucketName, String objectName, Integer expires, TimeUnit timeUnit);

    /**
     * 通过bucketName和objectName删除对象
     *
     * @param bucketName
     * @param objectName
     * @throws Exception
     */
    void removeObject(String bucketName, String objectName);

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return S3ObjectSummary 列表
     */
    List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);
}
