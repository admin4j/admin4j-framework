package com.admin4j.framework.signature;

import com.admin4j.framework.signature.annotation.Signature;
import com.admin4j.framework.signature.properties.SignatureProperties;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhougang
 * @since 2023/11/10 10:43
 */
public abstract class AbstractSignature implements SignatureService {

    private static final Logger log = LoggerFactory.getLogger(AbstractSignature.class);

    private final StringRedisTemplate stringRedisTemplate;

    private final SignatureProperties signatureProperties;

    private static final String SIGNATURE_NONCE_REDIS_KEY = "signature:nonce:";

    public AbstractSignature(StringRedisTemplate stringRedisTemplate, SignatureProperties signatureProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.signatureProperties = signatureProperties;
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
                StringUtils.isBlank(appSecret = getAppSecret(request.getHeader(signature.appId().filedName())))) {
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
        String serverSign = digestEncoder(plainText);
        // 客户端签名
        String clientSign = request.getHeader(signature.sign().filedName());
        if (!StringUtils.equals(clientSign, serverSign)) {
            return false;
        }
        String nonce = allParams.get(signature.nonce().filedName());
        // 将 nonce 记入缓存，防止重复使用（重点二：此处需要将 ttl 设定为允许 timestamp 时间差的值 x 2 ）
        stringRedisTemplate.opsForValue().set(SIGNATURE_NONCE_REDIS_KEY + nonce, nonce, signatureProperties.getExpireTime() * 2, TimeUnit.MILLISECONDS);
        return true;
    }

    /*
    private SignatureHeaders getSignatureHeaders(Signature signature, HttpServletRequest request) {
        SignatureHeaders signatureHeaders = new SignatureHeaders();
        signatureHeaders.setAppId(request.getHeader(signature.appId().filedName()));
        signatureHeaders.setAppId(request.getHeader(signature.timestamp().filedName()));
        signatureHeaders.setAppId(request.getHeader(signature.nonce().filedName()));
        signatureHeaders.setAppId(request.getHeader(signature.sign().filedName()));
        return signatureHeaders;
    }
     */

    /**
     * 1.appId是否合法，appId是否有对应的appSecret。
     * 2.请求是否已经超时，默认10分钟。
     * 3.随机串是否合法，是否在指定时间内已经访问过了。
     * 4.sign是否合法。
     */
    private boolean verifyHeaders(Signature signature, HttpServletRequest request) {

        String timestamp = request.getHeader(signature.timestamp().filedName());
        //Assert.notNull(timestamp, "timestamp cannot be empty");
        if (StringUtils.isBlank(timestamp)) {
            return false;
        }

        Long expireTime = signatureProperties.getExpireTime();
        //其他合法性校验
        long requestTimestamp = Long.parseLong(timestamp);
        // 检查 timestamp 是否超出允许的范围 （重点一：此处需要取绝对值）
        long timestampDisparity = Math.abs(System.currentTimeMillis() - requestTimestamp);
        //Assert.isTrue(!(timestampDisparity > expireTime), "Request time exceeds the specified limit");
        if (timestampDisparity > expireTime) {
            return false;
        }

        String nonce = request.getHeader(signature.nonce().filedName());
        //Assert.notNull(nonce, "Random strings cannot be empty");
        if (StringUtils.isBlank(nonce)) {
            return false;
        }
        //Assert.isTrue(!(nonce.length() < 10), "The random string nonce length is at least 10 bits");
        if (nonce.length() < 10) {
            return false;
        }
        String cacheNonce = stringRedisTemplate.opsForValue().get(SIGNATURE_NONCE_REDIS_KEY + nonce);
        //Assert.isNull(cacheNonce, "This nonce has already been used and the request is invalid");
        if (StringUtils.isNotBlank(cacheNonce)) {
            return false;
        }

        String sign = request.getHeader(signature.sign().filedName());
        //Assert.notNull(sign, "sign cannot be empty");
        return StringUtils.isNotBlank(sign);
    }

    /**
     * 获取全部参数(包括URL和Body上的)
     *
     * @param request    request
     * @return
     */
    protected SortedMap<String, String> getAllParams(Signature signature, HttpServletRequest request) throws IOException {

        SortedMap<String, String> sortedMap = new TreeMap<>();

        sortedMap.put(signature.appId().filedName(), request.getHeader(signature.appId().filedName()));
        sortedMap.put(signature.timestamp().filedName(), request.getHeader(signature.timestamp().filedName()));
        sortedMap.put(signature.nonce().filedName(), request.getHeader(signature.nonce().filedName()));
        // 有url带动态参数的情况, 所以加上url, 客户端对应也要拼接
        sortedMap.put("url", request.getServletPath());

        // 获取parameters（对应@RequestParam）
        if (!CollectionUtils.isEmpty(request.getParameterMap())) {
            Map<String, String[]> requestParams = request.getParameterMap();
            //获取GET请求参数,以键值对形式保存
            for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                sortedMap.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        // 分别获取了request input stream中的body信息、parameter信息
        JSONObject data = JSONObject.parseObject(requestWrapper.getBody());
        // 获取POST请求的JSON参数,以键值对形式保存
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sortedMap.put(entry.getKey(), entry.getValue().toString());
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

        // 摘要
        return plainText.toString();
    }

    /**
     * 获取appId对应的secret,假数据
     *
     * @param appId 应用id
     * @return
     */
    protected String getAppSecret(String appId) {
        return "";
    }

    /**
     * 摘要加密
     * @param plainText
     * @return
     */
    protected String digestEncoder(String plainText) throws IOException {
        return DigestUtils.md5DigestAsHex(StringUtils.getBytes(plainText, "UTF-8"));
    }
}
