package com.admin4j.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import com.admin4j.elasticsearch.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * @author andanyang
 * @since 2023/12/15 10:22
 */
@SpringBootTest
@Slf4j
public class ESDocTest {

    @Autowired
    ElasticsearchClient elasticsearchClient;
    String indexName = "products";


    // 插入文档
    @Test
    public void addDocument() throws IOException {
        Product product = new Product("bk-1", "City bike", 123.0, null);


        // IndexResponse response = elasticsearchClient.index(i -> i
        //         .index("products")
        //         .id(product.getSku())
        //         .document(product)
        // );
        //
        // log.info("Indexed with version " + response.version());

        IndexRequest<Product> request = IndexRequest.of(i -> i
                .index("products")
                .id(product.getSku())
                .document(product)
        );

        IndexResponse response = elasticsearchClient.index(request);

        log.info("Indexed with version " + response.version());
    }

    // 插入文档
    @Test
    public void addDocumentJson() throws IOException {

        Reader input = new StringReader(
                "{'@timestamp': '2022-04-08T13:55:32Z', 'level': 'warn', 'message': 'Some log message'}"
                        .replace('\'', '"'));

        IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index("logs")
                .withJson(input)
        );

        IndexResponse response = elasticsearchClient.index(request);

        log.info("Indexed with version " + response.version());
    }

    // 批量插入文档
    @Test
    public void addDocumentBulk() throws IOException {

        List<Product> products = Arrays.asList(
                new Product("bk-1", "City bike", 123.0, null),
                new Product("bk-2", "City bike", 124.0, null),
                new Product("bk-3", "City bike", 125.0, null),
                new Product("bk-4", "City bike", 126.0, null)
        );

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Product product : products) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("products")
                            .id(product.getSku())
                            .document(product)
                    )
            );
        }

        BulkRequest bulkRequest = br.build();
        log.error("bulkRequest {}", bulkRequest.toString());
        BulkResponse result = elasticsearchClient.bulk(bulkRequest);

        // Log errors, if any
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }

    @Test
    public void update() throws IOException {

        UpdateResponse updateResponse = elasticsearchClient.update(u -> u
                        .doc("products")
                        .id("bk-1"),
                Product.class);


        DeleteResponse deleteResponse = elasticsearchClient.delete(d -> d
                .index("products")
                .id("bk-1")
        );

    }

}
