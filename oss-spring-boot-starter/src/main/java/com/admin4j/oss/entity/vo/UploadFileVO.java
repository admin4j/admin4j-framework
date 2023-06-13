package com.admin4j.oss.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author andanyang
 * @since 2023/2/17 8:51
 */
@Data
@ApiModel("文件上传返回")
public class UploadFileVO {

    @ApiModelProperty("文件原始名称")
    private String originalFilename;
    private String bucket;
    @ApiModelProperty("文件大小 Bit")
    private long size;
    private LocalDateTime createTime;
    @ApiModelProperty("文件类型")
    private String contentType;
    private String md5;
    @ApiModelProperty("预览地址")
    private String previewUrl;
    @ApiModelProperty("文件前缀")
    private String prefix = "";
    @ApiModelProperty("文件key")
    private String key;
    /**
     * 文件保存成功之后的fileId
     */
    @ApiModelProperty("文件保存成功之后的fileId")
    private Long fileId;
    @ApiModelProperty("oss配置的名称")
    private String configName;
}
