package com.admin4j.framework.signature.core;

import lombok.Data;

@Data
public class SignatureHeaders {

    /**
     * 线下分配的值
     * 客户端和服务端各自保存appId对应的appSecret
     */
    private String appId;

    /**
     * 时间戳，单位: ms
     */
    private String timestamp;

    /**
     * 流水号【防止重复提交】; (备注：针对查询接口，流水号只用于日志落地，便于后期日志核查； 针对办理类接口需校验流水号在有效期内的唯一性，以避免重复请求)
     * => 流水号/随机串：至少16位，有效期内防重复提交
     */
    private String nonce;

    /**
     * 签名
     */
    private String sign;

    /**
     * 根据appId从服务端获取
     */
    private String appSecret;
}
