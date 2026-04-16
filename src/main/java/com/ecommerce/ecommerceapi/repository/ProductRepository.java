package com.ecommerce.ecommerceapi.repository;

import com.ecommerce.ecommerceapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long>{
    List<Product>findByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    // find products with stock greater than 0 (in stock)
    List<Product> findByStockGreaterThan(int stock);

    // pagination — get products page by page
    Page<Product> findAll(Pageable pageable);

    // pagination + filter by category
    Page<Product> findByCategory(String category, Pageable pageable);
}
