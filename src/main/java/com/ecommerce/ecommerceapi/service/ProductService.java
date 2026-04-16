package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.ProductRequest;
import com.ecommerce.ecommerceapi.dto.ProductResponse;
import com.ecommerce.ecommerceapi.exception.ResourceNotFoundException;
import com.ecommerce.ecommerceapi.model.Product;
import com.ecommerce.ecommerceapi.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // CREATE product
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        Product saved = productRepository.save(product);
        return convertToResponse(saved);
    }

    // GET all products with pagination
    public Page<ProductResponse> getAllProducts(
            int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortBy).ascending());
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToResponse);
    }

    // GET product by id
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product", "id", id));
        return convertToResponse(product);
    }

    // UPDATE product
    public ProductResponse updateProduct(
            Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product", "id", id));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        Product updated = productRepository.save(product);
        return convertToResponse(updated);
    }

    // DELETE product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product", "id", id));
        productRepository.delete(product);
    }

    // SEARCH products by keyword
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // GET products by category
    public List<ProductResponse> getByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // CONVERT Product entity to ProductResponse DTO
    public ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategory(product.getCategory());
        response.setCreatedAt(product.getCreatedAt());
        response.setStockStatus(
            product.getStock() > 0 ? "In Stock" : "Out of Stock");
        return response;
    }
}
