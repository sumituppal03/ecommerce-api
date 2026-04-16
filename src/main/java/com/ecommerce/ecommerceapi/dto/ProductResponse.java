package com.ecommerce.ecommerceapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
    private LocalDateTime createdAt;
    private String stockStatus;
}
