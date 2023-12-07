package com.admin4j.elasticsearch.autoconfigure;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import nl.altindag.ssl.SSLFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.net.URI;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/connecting.html
 *
 * @author andanyang
 * @since 2023/5/20 16:19
 */
@Data
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchAutoconfigure {

    /**
     * elasticsearch url.
     * 集群使用英文逗号隔开
     */
    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    @Bean
    @Lazy
    public ElasticsearchClient client(@Autowired(required = false) JsonpMapper jsonpMapper, @Autowired ObjectMapper objectMapper) {
        // 解析hostlist配置信息
        // 创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[elasticsearchProperties.getUris().size()];


        for (int i = 0; i < elasticsearchProperties.getUris().size(); i++) {
            String item = elasticsearchProperties.getUris().get(i);

            URI uri = URI.create(item);
            httpHostArray[i] = new HttpHost(uri.getHost(),
                    uri.getPort(), uri.getScheme());
        }


        RequestConfig.Builder requesrBuilder = RequestConfig.custom()
                .setConnectTimeout(Long.valueOf(elasticsearchProperties.getConnectionTimeout().toMillis()).intValue())
                .setSocketTimeout(Long.valueOf(elasticsearchProperties.getSocketTimeout().toMillis()).intValue());
        RequestConfig requestConfig = requesrBuilder.build();

        RestClientBuilder builder = RestClient.builder(httpHostArray);

        //
        SSLFactory sslFactory = SSLFactory.builder()
                .withUnsafeTrustMaterial()
                .withUnsafeHostnameVerifier()
                .build();

        // Create the low-level client
        // 添加认证
        if (elasticsearchProperties.getUsername() != null) {
            final CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticsearchProperties.getUsername(), elasticsearchProperties.getPassword()));

            builder.setHttpClientConfigCallback(f ->
                    f.setDefaultCredentialsProvider(provider)
                            .setSSLContext(sslFactory.getSslContext())
                            .setDefaultRequestConfig(requestConfig)
            );
        }

        if (elasticsearchProperties.getPathPrefix() != null) {
            builder.setPathPrefix(elasticsearchProperties.getPathPrefix());
        }


        RestClient restClient = builder.build();

        ElasticsearchTransport transport = null;
        if (jsonpMapper != null) {
            transport = new RestClientTransport(restClient, jsonpMapper);
        } else if (objectMapper != null) {
            transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));
        } else {
            transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        }
        // Create the transport with a Jackson mapper


        // And create the API client
        return new ElasticsearchClient(transport);
    }
}
