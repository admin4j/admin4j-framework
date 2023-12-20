package com.admin4j.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.admin4j.elasticsearch.entity.Product;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/12/15 10:22
 */
@SpringBootTest
@Slf4j
public class ESSearchTest {

    @Autowired
    ElasticsearchClient elasticsearchClient;
    String indexName = "products";

    @Test
    public void testSearch() throws Exception {

        GetResponse<Product> response = elasticsearchClient.get(g -> g
                        .index("products")
                        .id("bk-1"),
                Product.class
        );

        if (response.found()) {
            Product product = response.source();
            log.info("产品名称 " + product.getName());
        } else {
            log.info("未找到产品");
        }
    }

    @Test
    public void testSearchJSON() throws Exception {

        GetResponse<ObjectNode> response = elasticsearchClient.get(g -> g
                        .index("products")
                        .id("bk-1"),
                ObjectNode.class
        );

        if (response.found()) {
            ObjectNode json = response.source();
            String name = json.get("name").asText();
            log.info("产品名称 " + name);
        } else {
            log.info("未找到产品");
        }
    }

    @Test
    public void testSearch1() throws Exception {
        String searchText = "bk";

        SearchResponse<Product> response = elasticsearchClient.search(s -> s
                        .index("products")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        ),
                Product.class
        );

        TotalHits total = response.hits().total();// total可以获取结果的总数
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            log.info("找到 " + total.value() + " 个结果");
        } else {
            log.info("找到超过 " + total.value() + " 个结果");
        }


        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit : hits) {
            Product product = hit.source();
            log.info("找到产品 " + product.getSku() + "，得分 " + hit.score());
        }


    }
}
