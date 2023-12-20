package com.admin4j.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author andanyang
 * @since 2023/12/15 10:10
 */
@SpringBootTest
@Slf4j
public class ESIndexTest {

    @Autowired
    ElasticsearchClient elasticsearchClient;

    // 创建索引
    @Test
    public void createIndex() throws IOException {
        elasticsearchClient.indices().create(q -> q.index("products"));
    }

    // 判断索引是否存在并创建索引
    @Test
    public void indexExists() throws IOException {

        String iName = "products";
        // 获取【索引客户端对象】
        ElasticsearchIndicesClient indexClient = elasticsearchClient.indices();

        boolean flag = indexClient.exists(req -> req.index(iName)).value();
        // CreateIndexResponse createIndexResponse = null;
        boolean result = false;
        if (flag) {
            // 目标索引已存在
            log.info("索引【" + iName + "】已存在！");
        } else {
            // 不存在
            result = indexClient.create(req -> req.index(iName)).acknowledged();
            if (result) {
                log.info("索引【" + iName + "】创建成功！");
            } else {
                log.info("索引【" + iName + "】创建失败！");
            }
        }
    }

    // 判断索引是否存在并创建索引 构建器写法
    @Test
    public void indexExistsBuilder() throws IOException {

        String iName = "products";
        // 获取【索引客户端对象】
        ElasticsearchIndicesClient indexClient = elasticsearchClient.indices();
        // 1、构建【存在请求对象】
        ExistsRequest existsRequest = new ExistsRequest.Builder().index(iName).build();
        // 2、判断目标索引是否存在
        boolean flag = indexClient.exists(existsRequest).value();

        if (flag) {
            // 目标索引已存在
            log.info("索引【" + iName + "】已存在！");
        } else {
            // 1. 获取【创建索引请求对象】
            CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(iName).build();
            // 2. 创建索引，得到【创建索引响应对象】
            CreateIndexResponse createIndexResponse = indexClient.create(createIndexRequest);
            createIndexResponse = indexClient.create(req -> req.index(iName));
            // System.out.println("创建索引响应对象：" + createIndexResponse);
            boolean result = createIndexResponse.acknowledged();
            if (result) {
                log.info("索引【" + iName + "】创建成功！");
            } else {
                log.info("索引【" + iName + "】创建失败！");
            }
        }
    }

    // 查询索引
    @Test
    public void testSearchIndex() throws IOException {
        String indexName = "products";
        Map<String, IndexState> result = elasticsearchClient.indices().get(req -> req.index(indexName)).result();
        System.out.println("result = " + result);

        // 查询全部索引
        Set<String> all = elasticsearchClient.indices().get(req -> req.index("*")).result().keySet();
        System.out.println("all = " + all);

        // 删除索引
        Boolean isDelete = elasticsearchClient.indices().delete(req -> req.index(indexName)).acknowledged();
        if (isDelete) {
            log.info("删除索引成功");
        } else {
            log.info("删除索引失败");
        }
    }
}
