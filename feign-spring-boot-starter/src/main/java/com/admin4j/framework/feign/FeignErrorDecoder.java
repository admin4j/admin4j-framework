package com.admin4j.framework.feign;

import com.admin4j.framework.feign.exception.FallbackException;
import com.admin4j.json.JSONUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 默认不配置 fallback 会调用该类
 *
 * @author andanyang
 * @since 2023/12/21 16:07
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        String errorMsg = parseErrorMsg(response);
        if (StringUtils.hasText(errorMsg)) {
            return new FallbackException(errorMsg);
        } else {
            return new FallbackException("FeignErrorDecoder");
        }
    }

    /**
     * 解析错误消息
     *
     * @param response
     * @return
     */
    protected String parseErrorMsg(Response response) {

        try (InputStream inputStream = response.body().asInputStream()) {
            Map<String, Object> map = JSONUtil.parseMap(inputStream);
            return (String) map.get("msg");
        } catch (IOException e) {
            throw new FallbackException(e);
        }
    }
}
