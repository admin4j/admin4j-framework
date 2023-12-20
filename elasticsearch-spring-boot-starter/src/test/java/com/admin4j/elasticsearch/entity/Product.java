package com.admin4j.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author andanyang
 * @since 2023/12/15 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String sku;
    private String name;
    private double price;
    private String description;
}
