package com.admin4j.oss;


import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 一个模拟 MultipartFile 的类
 *
 * @author andanyang
 * @since 2021/11/11 11:12
 */
@Getter
public class MockMultipartFile implements MultipartFile {


    /**
     * 文件名
     */
    private final String name;

    /**
     * 原始文件名
     */
    private final String originalFilename;

    /**
     * 内容类型
     */
    @Nullable
    private final String contentType;

    /**
     * 文件内容
     */
    private final byte[] bytes;


    public MockMultipartFile(String name, InputStream in) throws IOException {
        this(name, "", null, IOUtils.toByteArray(in));
    }

    public MockMultipartFile(String name, @Nullable byte[] bytes) {
        this(name, "", null, bytes);
    }

    public MockMultipartFile(String name, @Nullable String originalFilename, @Nullable String contentType, InputStream in) throws IOException {
        this(name, originalFilename, contentType, IOUtils.toByteArray(in));
    }

    public MockMultipartFile(@Nullable String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable byte[] bytes) {
        this.name = (name != null ? name : "");
        this.originalFilename = (originalFilename != null ? originalFilename : "");
        this.contentType = contentType;
        this.bytes = (bytes != null ? bytes : new byte[0]);
    }


    @Override
    public boolean isEmpty() {
        return (this.bytes.length == 0);
    }

    @Override
    public long getSize() {
        return this.bytes.length;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        FileUtils.writeByteArrayToFile(dest, bytes);
    }
}
