package com.admin4j.oss.impl;

import com.admin4j.oss.OssProperties;
import com.admin4j.oss.OssTemplate;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2023/4/13 15:25
 */
@RequiredArgsConstructor
public class OssTemplateImpl implements OssTemplate {

    private final AmazonS3 amazonS3;
    private final OssProperties ossProperties;

    /**
     * 创建Bucket
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateBucket.html
     *
     * @param bucketName bucket名称
     */
    @Override
    public void createBucket(String bucketName) {
        bucketName = defaultBucketName(bucketName);
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    /**
     * 获取所有的buckets
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListBuckets.html
     *
     * @return Bucket list
     */
    @Override
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    /**
     * 通过Bucket名称删除Bucket
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteBucket.html
     *
     * @param bucketName
     */
    @Override
    public void removeBucket(String bucketName) {
        bucketName = defaultBucketName(bucketName);
        amazonS3.deleteBucket(bucketName);
    }

    /**
     * 上传对象
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param contextType 文件类型
     *                    AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
     */
    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, String contextType) throws IOException {
        return putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    /**
     * 上传对象
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     *                   AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
     */
    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream) throws IOException {
        return putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 通过bucketName和objectName获取对象
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html
     */
    @Override
    public S3Object getObject(String bucketName, String objectName) {
        bucketName = defaultBucketName(bucketName);
        return amazonS3.getObject(bucketName, objectName);
    }

    /**
     * 获取对象的url
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_GeneratePresignedUrl.html
     */
    @Override
    public String getObjectURL(String bucketName, String objectName, Integer expires, TimeUnit timeUnit) {

        bucketName = defaultBucketName(bucketName);
        long millis = timeUnit.toMillis(expires);
        millis = System.currentTimeMillis() + millis;
        Date date = new Date(millis);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, date);
        return url.toString();
    }

    /**
     * 通过bucketName和objectName删除对象
     *
     * @param bucketName
     * @param objectName AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteObject.html
     */
    @Override
    public void removeObject(String bucketName, String objectName) {
        bucketName = defaultBucketName(bucketName);
        amazonS3.deleteObject(bucketName, objectName);
    }

    /**
     * 根据bucketName和prefix获取对象集合
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjects.html
     */
    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        bucketName = defaultBucketName(bucketName);
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }


    /**
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contextType
     * @return 返回结果
     */
    private PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
                                      String contextType) throws IOException {

        bucketName = defaultBucketName(bucketName);
        byte[] bytes = IOUtils.toByteArray(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 上传
        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
    }

    private String defaultBucketName(String bucketName) {
        if (bucketName == null || bucketName.equals("")) {
            return ossProperties.getDefaultBucketName();
        }
        return bucketName;
    }
}
