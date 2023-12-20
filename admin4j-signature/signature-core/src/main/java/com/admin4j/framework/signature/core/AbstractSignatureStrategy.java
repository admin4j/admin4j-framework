package com.admin4j.framework.signature.core;

import com.admin4j.framework.signature.core.annotation.Signature;
import com.admin4j.framework.signature.core.properties.SignatureProperties;
import com.admin4j.framework.signature.core.util.SignatureUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhougang
 * @since 2023/11/10 10:43
 */
public abstract class AbstractSignatureStrategy implements SignatureStrategy {

    private static final Logger log = LoggerFactory.getLogger(AbstractSignatureStrategy.class);

    private final StringRedisTemplate stringRedisTemplate;

    private final SignatureProperties signatureProperties;

    private final SignatureApi signatureApi;

    public AbstractSignatureStrategy(StringRedisTemplate stringRedisTemplate,
                                     SignatureProperties signatureProperties,
                                     SignatureApi signatureApi) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.signatureProperties = signatureProperties;
        this.signatureApi = signatureApi;
    }

    /**
     * 判断请求是否签名通过
     *
     * @param request HttpServletRequest
     * @return 是否通过
     */
    @Override
    public boolean verify(Signature signature, HttpServletRequest request) throws IOException {
        // 根据appId获取appSecret
        String appId = request.getHeader(signature.appId().filedName());
        String appSecret;
        if (StringUtils.isBlank(appId) ||
                StringUtils.isBlank(appSecret = signatureApi.getAppSecret(appId))) {
            return false;
        }
        // 根据request 中 header值生成SignatureHeaders实体
        if (!verifyHeaders(signature, request)) {
            return false;
        }
        // 获取全部参数(包括URL和Body上的)
        SortedMap<String, String> allParams = getAllParams(signature, request);
        // 生成服务端签名
        String plainText = paramsSplicing(allParams, appSecret);
        // 将digest 转换成UTF-8 的 byte[] 后 使用MD5算法加密，最后将生成的md5字符串
        String serverSign = signatureApi.digestEncoder(plainText);
        // 客户端签名
        String clientSign = request.getHeader(signature.sign().filedName());
        if (!StringUtils.equals(clientSign, serverSign)) {
            return false;
        }
        String nonce = allParams.get(signature.nonce().filedName());
        // 将 nonce 记入缓存，防止重复使用（重点二：此处需要将 ttl 设定为允许 timestamp 时间差的值 x 2 ）
        stringRedisTemplate.opsForValue().set(signatureProperties.getSignatureNonceCacheKey() + nonce, nonce, signatureProperties.getExpireTime() * 2, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 1.appId是否合法，appId是否有对应的appSecret。
     * 2.请求是否已经超时，默认10分钟。
     * 3.随机串是否合法，是否在指定时间内已经访问过了。
     * 4.sign是否合法。
     */
    private boolean verifyHeaders(Signature signature, HttpServletRequest request) {

        String timestamp = request.getHeader(signature.timestamp().filedName());
        if (StringUtils.isBlank(timestamp)) {
            return false;
        }

        Long expireTime = signatureProperties.getExpireTime();
        // 其他合法性校验
        long requestTimestamp = Long.parseLong(timestamp);
        // 检查 timestamp 是否超出允许的范围 （重点一：此处需要取绝对值）
        long timestampDisparity = Math.abs(System.currentTimeMillis() - requestTimestamp);
        if (timestampDisparity > expireTime) {
            return false;
        }

        String nonce = request.getHeader(signature.nonce().filedName());
        if (StringUtils.isBlank(nonce)) {
            return false;
        }
        if (nonce.length() < 10) {
            return false;
        }
        String cacheNonce = stringRedisTemplate.opsForValue().get(signatureProperties.getSignatureNonceCacheKey() + nonce);
        if (StringUtils.isNotBlank(cacheNonce)) {
            return false;
        }

        String sign = request.getHeader(signature.sign().filedName());
        return StringUtils.isNotBlank(sign);
    }

    /**
     * 获取全部参数(包括URL和Body上的)
     *
     * @param request request
     * @return SortedMap
     */
    protected SortedMap<String, String> getAllParams(Signature signature, HttpServletRequest request) throws IOException {

        SortedMap<String, String> sortedMap = new TreeMap<>();
        if (signature.appId().enable()) {
            sortedMap.put(signature.appId().filedName(), request.getHeader(signature.appId().filedName()));
        }
        if (signature.uri().enable()) {
            // 例：/user/{id}  有uri带动态参数的情况, 此字段不用传递到后端，但是需要用这个字段参与签名
            sortedMap.put(signature.uri().filedName(), request.getServletPath());
        }
        if (signature.timestamp().enable()) {
            sortedMap.put(signature.timestamp().filedName(), request.getHeader(signature.timestamp().filedName()));
        }
        if (signature.nonce().enable()) {
            sortedMap.put(signature.nonce().filedName(), request.getHeader(signature.nonce().filedName()));
        }

        // 获取parameters（对应@RequestParam）
        if (!CollectionUtils.isEmpty(request.getParameterMap())) {
            Map<String, String[]> requestParams = request.getParameterMap();
            //获取GET请求参数,以键值对形式保存
            SortedMap<String, String> paramMap = new TreeMap<>();
            for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                paramMap.put(entry.getKey(), entry.getValue()[0]);
            }
            sortedMap.put("param", JSON.toJSONString(paramMap));
        }

        CacheHttpServletRequestWrapper requestWrapper = new CacheHttpServletRequestWrapper(request);
        // 分别获取了request input stream中的body信息、parameter信息
        String body = new String(requestWrapper.getBody(), StandardCharsets.UTF_8);
        if (StringUtils.isNotBlank(body)) {
            // body可能为JSON对象或JSON数组
            if (JSON.isValidObject(body)) {
                JSONObject jsonObj = JSONObject.parseObject(body);
                // 获取POST请求的JSON参数,以键值对形式保存
                sortedMap.put("body", JSON.toJSONString(SignatureUtil.traverseMap(jsonObj)));
            } else if (JSON.isValidArray(body)) {
                JSONArray jsonArray = JSONArray.parseArray(body);
                sortedMap.put("body", JSON.toJSONString(SignatureUtil.traverseList(jsonArray)));
            } else {
                sortedMap.put("body", body);
            }
        }
        return sortedMap;
    }

    /**
     * 所有的参数与应用密钥appSecret 进行排序加密后生成签名
     *
     * @param sortedMap 根据key升序排序的后所有请求参数
     * @param appSecret 应用id对应的应用密钥
     * @return 生成接口签名
     */
    protected String paramsSplicing(SortedMap<String, String> sortedMap, String appSecret) {
        // 进行key value拼接
        StringBuilder plainText = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            plainText.append(entry.getKey()).append(entry.getValue());
        }
        // 结尾拼接应用密钥 appSecret
        plainText.append(appSecret);
        return plainText.toString();
    }
}
