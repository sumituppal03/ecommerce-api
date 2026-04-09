package com.ecommerce.ecommerceapi.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import lombok.Data;
@Entity
@Data
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product is required")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0",inclusive = false,message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Stock is required")
    @Min(value = 0,message = "Stock cannot be negative")
    @Column(nullable = false)
    private Integer stock;

    @NotBlank(message = "Category is required")
    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
       createdAt = LocalDateTime.now();
    }

}
