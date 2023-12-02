package com.admin4j.framework.signature.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * 签名API接口，请自定义实现类
 *
 * @author zhougang
 * @since 2023/12/02 20:28
 */
public interface SignatureApi {

    /**
     * 提供密钥
     *
     * @param appId 应用ID
     * @return appSecret
     */
    String getAppSecret(String appId);

    /**
     * 摘要加密，默认实现md5加密
     *
     * @param plainText 明文
     * @return Sign
     */
    default String digestEncoder(String plainText) {
        try {
            return DigestUtils.md5DigestAsHex(StringUtils.getBytes(plainText, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
