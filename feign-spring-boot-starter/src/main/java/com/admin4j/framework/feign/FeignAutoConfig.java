package com.admin4j.framework.feign;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2022/7/29 11:47
 */
public class FeignAutoConfig {


    /**
     * 自定义OK HTTP Client的参数
     *
     * @return
     */
    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 读取超时时间（不包含解析地址，提交请求的耗时）
        builder.readTimeout(10, TimeUnit.SECONDS);
        // 写入超时时间
        builder.writeTimeout(10, TimeUnit.SECONDS);
        // 连接远程服务器超时时间
        builder.connectTimeout(10, TimeUnit.SECONDS);
        // 如果连接远程服务器失败是否重试
        builder.setRetryOnConnectionFailure$okhttp(true);
        // 当HTTP返回码为3xx（重定向）时，是否执行重定向操作
        builder.setFollowRedirects$okhttp(true);
        builder.setFollowSslRedirects$okhttp(true);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new FeignLogger("FeignLogger"));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        return builder;
    }
}
